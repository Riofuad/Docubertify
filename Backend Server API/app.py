from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse
import uvicorn
import torch
from transformers import BertTokenizer, BertForSequenceClassification
import google.generativeai as genai
import os
import json
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

app = FastAPI()

# Initialize BERT model and tokenizer
model_path = 'bert_model.pt'
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased')
model = BertForSequenceClassification.from_pretrained('bert-base-multilingual-cased', num_labels=2)
model.load_state_dict(torch.load(model_path))
model.eval()

# Configure Gemini API
api_key = os.getenv("GOOGLE_API_KEY")
if api_key is None:
    raise ValueError("GOOGLE_API_KEY environment variable is not set")
genai.configure(api_key=api_key)

# Define label mapping
label_mapping = {0: 'KTP', 1: 'SIM'}

def get_document_json_schema(document_type: str) -> str:
    if document_type == "KTP":
        return '''
{
    "document_type": "KTP",
    "data": {
        "nik": "",
        "nama": "",
        "tempat_tanggal_lahir": "",
        "jenis_kelamin": "",
        "gol_darah": "",
        "alamat": "",
        "rt_rw": "",
        "kel_desa": "",
        "kecamatan": "",
        "agama": "",
        "status_perkawinan": "",
        "pekerjaan": "",
        "kewarganegaraan": "",
        "berlaku_hingga": ""
    }
}
'''
    else:
        return '''
{
    "document_type": "SIM",
    "data": {
        "kelas_lisensi": "",
        "no_lisensi": "",
        "nama": "",
        "tempat_tanggal_lahir": "",
        "gol_darah": "",
        "jenis_kelamin": "",
        "alamat": "",
        "pekerjaan": "",
        "domisili": "",
        "berlaku_hingga": ""
    }
}
'''


def prompting(document_type: str, document_extracted_text: str) -> str:
    json_type = get_document_json_schema(document_type)
    prompt = f'''
Make this text to a JSON with {document_type} document content below:
"{document_extracted_text}"
'''
    return prompt + json_type

async def query_gemini_api(prediction: str, text: str) -> dict:
    prompt = prompting(prediction, text)
    try:
        # Initialize the Gemini model for text-based tasks
        model = genai.GenerativeModel(model_name="gemini-1.5-flash")
        response = model.generate_content([prompt])
        
        # Clean the response text and parse it as JSON
        raw_response = response.text
        print(f"Raw Gemini API response: {raw_response}")  # Debug line

        # Remove markdown and extra characters (if necessary)
        cleaned_response = raw_response.split('```json')[-1].split('```')[0].strip()
        
        # Parse the cleaned response to JSON
        response_json = json.loads(cleaned_response)
        
        return response_json
    except Exception as e:
        print(f"Error during Gemini API query: {e}")  # Debug line
        raise HTTPException(status_code=500, detail=str(e))

def perform_model_inference(text: str):
    try:
        # Tokenisasi teks input
        inputs = tokenizer(text, return_tensors='pt', truncation=True, padding=True, max_length=128)
        input_ids = inputs['input_ids']
        attention_mask = inputs['attention_mask']

        # Melakukan inferensi
        with torch.no_grad():
            outputs = model(input_ids, attention_mask=attention_mask)
            logits = outputs.logits
            predicted_class = torch.argmax(logits, dim=1).item()

        # Mengembalikan label prediksi
        prediction_label = label_mapping[predicted_class]
        return prediction_label

    except Exception as e:
        raise ValueError(f"Error during model inference: {str(e)}")

@app.post("/predict/")
async def predict(text: str):
    try:
        prediction_label = perform_model_inference(text)

        # Query Gemini API
        gemini_response = await query_gemini_api(prediction_label, text)

        # Return the prediction and Gemini API response
        return JSONResponse(content={"prediction": prediction_label, "gemini_response": gemini_response})

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

# uvicorn app:app --host 0.0.0.0 --port 8000
# ngrok http --domain=haddock-prime-probably.ngrok-free.app 8000
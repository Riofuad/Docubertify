# **Docubertify**  
This repository contains the source code from my college thesis entitled **"Design and Development of an Android-Based Document Classification and Digitization Application Using ML Kit SDKs and BERT Algorithm"**.

## Table of Contents
- [Project Overview](#project-overview)
- [Folder Structure](#folder-structure)
- [Requirements](#requirements)
- [Datasets for Fine-Tuned BERT Model](#datasets-for-fine-tuned-bert-model)
- [How To Run Backend Server API](#how-to-run-backend-server-api)
- [Android App Preview](#android-app-preview)

## Project Overview
<p align="center">
   <img src="https://github.com/user-attachments/assets/544da5de-e027-4bda-a986-3a2b01845675" alt="docubertify_logo" width="400">
</p>

This project focuses on automating document classification (KTP and SIM) and digitizing the content using machine learning. It uses the BERT model for text classification and Google's ML Kit SDKs for text recognition. The application is built for Android devices, and it includes a backend server to handle API requests for document classification and a pre-trained BERT model.

## Folder Structure
The repository is organized into the following main directories:

```bash
.
├── Backend Server API/   # Contains FastAPI code and API endpoints
├── Frontend Android/     # Android app code (Kotlin) with ML Kit integration
└── ML BERT Model/        # Pre-trained BERT model, training scripts, and model fine-tuning
```
### Backend Server
This folder contains the source code for the FastAPI server. The API handles requests from the Android app, processes document text, and performs document classification using the BERT model.

### Frontend Android
The Android application, built with Kotlin, allows users to capture or select documents (KTP/SIM), perform text recognition using ML Kit SDKs, and send extracted text to the backend API for classification.

### ML BERT Model
This folder includes:
- **Datasets:** CSV files containing text data for document classification (e.g., KTP and SIM).
- **JSON Dict:** A dictionary for filtering and fixing typos in text extracted from the documents.
- **Jupyter Notebook (.ipynb):** The notebook used for training and fine-tuning the BERT model.

## Requirements
- [Python 3.11.0+](https://www.python.org/)
- [Pytorch](https://pytorch.org/)
- [Google Colaboratory](https://colab.google/)
- [Visual Studio Code](https://code.visualstudio.com/)
- [FastAPI](https://fastapi.tiangolo.com/)
- [Ngrok (Static Domain)](https://ngrok.com/)
- [Gemini API](https://ai.google.dev/gemini-api/docs)
- [Android Studio Iguana // 2023.2.1+](https://developer.android.com/studio)
- Kotlin 232-1.9.0-release-358-AS10227.8.2321.11479570+ (Java 11.0.20)
- [ML Kit SDKs (_Text Recognition v2_)](https://developers.google.com/ml-kit/vision/text-recognition/v2/android?hl=en)

## Datasets for Fine-Tuned BERT Model
<p align="center">
   <img src="https://github.com/user-attachments/assets/dd418e3b-2f72-4583-b3cb-c96bdd9e9ea5" alt="ktp_dummy_image" width="300"> 
   <img src="https://github.com/user-attachments/assets/af4c8f9a-4658-4116-ae9a-a0cbad177709" alt="sim_dummy_image" width="300">
</p>


Because the dataset used to build the fine-tuned BERT model is sensitive, which is the image dataset of KTP (ID Card) and SIM (Driving License) documents of the Republic of Indonesia, the data used is in the form of dummy document images for KTP and SIM made in Figma. The dataset amounts to 1000 dummy images with 500 images each for each type of document. The dataset used can be seen [here](https://www.figma.com/design/OtIwQ9dmDyrZ7wEjJyQpIY/Datasets?node-id=0-1&t=4thcqfgBOaZ1OpCk-1).

⚠️ **Disclaimer** ⚠️
--
The dataset is only used for research needs and although the document images are dummy (not based on data from real people, only random generation) still use the KTP and SIM image dataset wisely. Violations that occur due to misuse of KTP and SIM document images are beyond the responsibility of the author.

## How To Run Backend Server API
Before using the app, the API server must be running by following these steps:
1. **Run the API locally using Uvicorn**  
   Start the FastAPI server on your local machine:
   ```bash
   uvicorn app:app --host 0.0.0.0 --port 8000
   ```
2. **Run Ngrok to expose the server using a static domain**  
   You need to expose your local server to the internet using Ngrok with a static domain. Generate a static domain from the [Ngrok dashboard](https://dashboard.ngrok.com/domains), then run the following command:
   ```bash
   ngrok http --domain=[static-domain] 8000
   ```
   For more information on how to generate static domains for Ngrok, you can visit this [Ngrok blog post](https://ngrok.com/blog-post/free-static-domains-ngrok-users).

## Android App Preview
MainActivity | Select Image <br>(Camera Option) | Select Image <br>(Gallery Option) | Crop Image |
--------------|------------------------------|-------------------------------|------------|
![MainActivity](https://github.com/user-attachments/assets/d8a1ebfd-fc99-47a1-8dfd-18230594f50a)|![Select Image (Camera Option)](https://github.com/user-attachments/assets/bfc53e00-e068-4341-abf0-b2264e6fca93)|![Select Image (Gallery Option)](https://github.com/user-attachments/assets/ca7c63f3-2e3c-4732-8679-2104880391ef)|![CropImage](https://github.com/user-attachments/assets/03ca6d09-6376-4b86-91e5-4598381e37fd)|

ResultActivity | ResultActivity <br>(Loading State) | ResultActivity <br>(Bottom Sheet Fragment Show Half) |
---------------|--------------------------------|--------------------------------------------------|
![ResultActivity](https://github.com/user-attachments/assets/57652803-5a9a-4110-939e-2dd7b380acce)|![ResultActivity (Loading)](https://github.com/user-attachments/assets/5033b4aa-5e85-4d6e-a376-3d4c0d52fd2f)|![ResultActivity (Bottom Sheet Fragment Showing)](https://github.com/user-attachments/assets/0a4f4a14-f2b7-49cd-8386-44ace14fd5f0)|

KtpBottomSheetFragment | SimBottomSheetFragment |
-----------------------|------------------------|
![Bottom Sheet Fragment KTP](https://github.com/user-attachments/assets/a7efa694-3b02-4974-99d3-94308335e49f)|![Bottom Sheet Fragment SIM](https://github.com/user-attachments/assets/21dec024-cee8-4ccd-974b-cf0b58bc5bd8)|










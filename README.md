# Docubertify

# Document Classification and Digitization Application using BERT and ML Kit SDKs

This repository contains the source code for a document classification and digitization application built using Android (Kotlin) for the frontend, a FastAPI backend server, and a BERT model for text classification.

## Table of Contents
- [Project Overview](#project-overview)
- [Folder Structure](#folder-structure)
- [Requirements](#requirements)
- [How To Run Backend Server API](#how-to-run-backend-server-api)

## Project Overview
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

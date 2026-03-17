# AI Fake Image Detector 🕵️‍♂️

A full-stack web application that leverages machine learning to analyze images and detect if they are AI-generated or real. This project integrates a Java Spring Boot backend with a responsive, modern frontend, utilizing the Hugging Face Inference API for real-time model analysis.

## 🚀 Tech Stack
* **Backend:** Java 17, Spring Boot, Maven
* **Frontend:** HTML5, Vanilla JavaScript, Tailwind CSS
* **AI Integration:** Hugging Face Inference API (`umm-maybe/AI-image-detector`)
* **DevOps:** Docker

## ✨ Features
* **Seamless UI:** Drag-and-drop file upload interface styled with Tailwind CSS.
* **Secure API Handling:** Backend proxy architecture prevents exposing secret Hugging Face API tokens to the client browser.
* **Network Resiliency:** Implemented custom headers (User-Agent masking) to bypass strict Cloudflare/ISP firewalls on free-tier AI APIs.
* **Error Handling:** Graceful JSON error parsing and frontend alerts for API timeouts or payload limits.

## 🛠️ Setup & Local Development

### Prerequisites
1. A free [Hugging Face](https://huggingface.co/) account and Access Token.
2. Docker (Optional, for containerized execution).
3. Java 17 and Maven (If running from source).

### Note if it doesnt work change server.port in application properties to whatever port you like !
### [Live hosted app](https://fake-image-detection-mjqy.onrender.com/) ps wait 1-1.5 mins as the app takes time to boot up 

### Method 1: Run via Docker (Recommended)
You can pull and run the pre-built Docker image instantly. Remember to swap in your actual Hugging Face token!

```bash
docker run -p 8080:8080 -e HF_API_TOKEN="your_token_here" yourusername/fake-image-detector:v1

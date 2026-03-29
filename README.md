# 🔍 AI Image Forensics (Gemini Multimodal)

A full-stack web application designed to detect AI-generated images by analyzing pixel artifacts, illogical geometry, and AI watermarks. 

This is **Version 2** of the project, completely rebuilt to leverage the reasoning capabilities of Large Language Models (LLMs) rather than traditional Convolutional Neural Networks (CNNs). 

## 🚀 Why Gemini over standard CNNs?
Traditional Hugging Face classification models rely strictly on pixel-level mathematical noise. As image generators (like Midjourney and DALL-E) improve, they leave fewer pixel artifacts, rendering older CNNs ineffective. 

This backend utilizes the **Google Gemini Multimodal API**, allowing the system to use actual logical reasoning (e.g., reading a "Craiyon" watermark, noticing 7 fingers on a hand, or identifying structurally impossible buildings) to determine authenticity.

## 💻 Tech Stack
* **Backend:** Java, Spring Boot, Maven
* **AI Integration:** Google Gemini 2.5 Flash API (Multimodal)
* **Frontend:** Vanilla JavaScript, HTML5, Tailwind CSS (Glassmorphism UI)
* **Architecture:** RESTful API, Separation of Concerns (MVC pattern)

## ⚙️ Local Setup & Installation

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/Fake-Image-Detector-Gemini.git]
   (https://github.com/yourusername/Fake-Image-Detector-Gemini.git)
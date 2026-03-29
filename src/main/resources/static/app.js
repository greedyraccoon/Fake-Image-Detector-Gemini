// src/main/resources/static/js/app.js

function previewImage(event) {
    const input = event.target;
    const fileNameSpan = document.getElementById('fileName');
    const previewContainer = document.getElementById('previewContainer');
    const imagePreview = document.getElementById('imagePreview');

    if (input.files && input.files[0]) {
        fileNameSpan.textContent = input.files[0].name;
        const reader = new FileReader();
        reader.onload = function(e) {
            imagePreview.src = e.target.result;
            previewContainer.classList.remove('hidden');
        }
        reader.readAsDataURL(input.files[0]);
    }
}

async function analyzeImage() {
    const fileInput = document.getElementById('imageInput');
    const resultCard = document.getElementById('resultCard');
    const finalVerdict = document.getElementById('finalVerdict');
    const confidenceBar = document.getElementById('confidenceBar');
    const reasonText = document.getElementById('reasonText');
    
    const analyzeBtn = document.getElementById('analyzeBtn');
    const btnText = document.getElementById('btnText');
    const loadingSpinner = document.getElementById('loadingSpinner');

    if (fileInput.files.length === 0) {
        alert('Please select an image first!');
        return;
    }

    // UI Loading State
    analyzeBtn.disabled = true;
    btnText.textContent = "Analyzing Pixels...";
    loadingSpinner.classList.remove('hidden');
    resultCard.classList.add('hidden');
    confidenceBar.style.width = '0%';

    const formData = new FormData();
    formData.append('image', fileInput.files[0]);

    try {
        const response = await fetch('/api/v1/detect', {
            method: 'POST',
            body: formData
        });
        
        const result = await response.json();
        
        if (result.error) {
            throw new Error(result.error);
        }

        const topPrediction = result[0];
        const scorePercent = (topPrediction.score * 100).toFixed(1);
        
        // Show Result Card
        resultCard.classList.remove('hidden');
        confidenceBar.style.width = `${scorePercent}%`;

        // Build the Dynamic Text
        if (topPrediction.label === 'artificial') {
            finalVerdict.textContent = "AI GENERATED";
            finalVerdict.className = "text-3xl font-black mb-2 tracking-wide text-red-500 drop-shadow-[0_0_8px_rgba(239,68,68,0.8)]";
            confidenceBar.className = "bg-red-500 h-2.5 rounded-full transition-all duration-1000 ease-out";
            reasonText.innerHTML = `This image is <span class="font-bold text-red-400">${scorePercent}%</span> likely to be AI. <br/> <span class="text-gray-400">Analysis: ${topPrediction.reason}</span>`;
        } else {
            finalVerdict.textContent = "AUTHENTIC IMAGE";
            finalVerdict.className = "text-3xl font-black mb-2 tracking-wide text-emerald-400 drop-shadow-[0_0_8px_rgba(52,211,153,0.8)]";
            confidenceBar.className = "bg-emerald-400 h-2.5 rounded-full transition-all duration-1000 ease-out";
            reasonText.innerHTML = `This image is <span class="font-bold text-emerald-400">${scorePercent}%</span> likely to be an authentic photograph. <br/> <span class="text-gray-400">Analysis: ${topPrediction.reason || 'No AI artifacts detected.'}</span>`;
        }

    } catch (error) {
        resultCard.classList.remove('hidden');
        finalVerdict.textContent = "SYSTEM ERROR";
        finalVerdict.className = "text-3xl font-black mb-2 tracking-wide text-yellow-500";
        reasonText.textContent = error.message;
    } finally {
        // Reset Button UI
        analyzeBtn.disabled = false;
        btnText.textContent = "Run Forensics Analysis";
        loadingSpinner.classList.add('hidden');
    }
}

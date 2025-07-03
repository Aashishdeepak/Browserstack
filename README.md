# BrowserStack Technical Assignment

## ✅ Overview

This script demonstrates web scraping, API integration, text processing, and cross-browser testing using Selenium + BrowserStack.

## 🔧 Functionality

- Scrapes top 5 opinion articles from [El País](https://elpais.com/)
- Extracts Spanish titles, content, and images
- Translates titles using RapidAPI
- Detects repeated words from translated headers
- Runs tests locally and in parallel on BrowserStack

## 🚀 Running the Project

### 1. Clone and Install Dependencies

```bash
git clone https://github.com/IKG12345/BrowserStackTechnicalAssignment.git
cd BrowserStackTechnicalAssignment
pip install -r requirements.txt
```

### 2. Add Your Secrets to GitHub

- `RAPIDAPI_KEY`
- `BROWSERSTACK_USERNAME`
- `BROWSERSTACK_ACCESS_KEY`

### 3. Run Locally

```bash
python main.py
```

### 4. CI/CD with GitHub Actions

On push or PR to `main`, `.github/workflows/browserstack.yml` will trigger parallel testing using your credentials.

## 📁 Output

- Titles, content printed to console
- Images saved to `/assets`
- Translations & analysis shown clearly

---
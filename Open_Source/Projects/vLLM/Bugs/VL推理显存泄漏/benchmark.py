import base64
import os
import json
import requests
from pathlib import Path
import mimetypes


class QwenVLInference:

    def __init__(self, base_url="http://localhost:8000/v1"):
        self.base_url = base_url
        self.chat_completion_url = f"{base_url}/chat/completions"
    
    def encode_image(self, image_path):
        try:
            with open(image_path, "rb") as image_file:
                encoded_string = base64.b64encode(image_file.read()).decode('utf-8')

                mime_type, _ = mimetypes.guess_type(image_path)
                if mime_type is None:
                    mime_type = "image/jpeg"
                
                data_url = f"data:{mime_type};base64,{encoded_string}"
                return data_url
        except Exception as e:
            print(f"Error encoding image {image_path}: {e}")
            return None
    
    def send_request(self, image_path, prompt="描述这张图片的内容？"):
        print(f"Processing image: {image_path}")

        image_data = self.encode_image(image_path)
        if image_data is None:
            return None
        
        payload = {
            "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct",
            "messages": [
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": [
                    {"type": "image_url", "image_url": {"url": image_data}},
                    {"type": "text", "text": "What is the text in the illustrate?"}
                ]}
            ],
            "max_tokens": 1024
        }

        try:
            response = requests.post(
                self.chat_completion_url,
                headers={"Content-Type": "application/json"},
                json=payload,
                timeout=60,
            )

            if response.status_code == 200:
                result = response.json()
                return result
            else:
                print(f"Error: HTTP {response.status_code} - {response.text}")
                return None
        except requests.exceptions.RequestException as e:
            print(f"Request failed: {e}")
            return None
        
    def process_folder_images(self, folder_path):
        folder = Path(folder_path)

        image_extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"}

        image_files = []
        for ext in image_extensions:
            image_files.extend(folder.glob(f"*{ext}"))
            image_files.extend(folder.glob(f"*{ext.upper()}"))
        
        if not image_files:
            print(f"No image files found in {folder_path}.")
            return []
        
        print(f"Found {len(image_files)} image files.")

        results = []
        for image_path in image_files:
            print(f"\n{'-' * 100}")
            print(f"Processing: {image_path.name}")

            result = self.send_request(image_path)
            if result:
                if "choices" in result and len(result["choices"]) > 0:
                    content = result["choices"][0]["message"]["content"]
                    print(f"Response: {content}")

                    results.append({
                        "image": image_path.name,
                        "response": content,
                    })
                else:
                    print("No response in result.")
            else:
                print(f"Failed to process {image_path.name}")
        
        return results
    
    def save_results(self, results, output_file="vl_bench_results.json"):
        if results:
            with open(output_file, "w", encoding="utf-8") as f:
                json.dump(results, f, ensure_ascii=False, indent=2)
            print(f"\nResults saved to {output_file}")


def main():
    IMAGE_PATH = "/home/sss/images-2"

    client = QwenVLInference()

    results = client.process_folder_images(IMAGE_PATH)

    if results:
        client.save_results(results)


if __name__ == "__main__":
    main()

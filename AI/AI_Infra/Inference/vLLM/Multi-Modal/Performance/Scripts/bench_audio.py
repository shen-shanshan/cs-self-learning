import base64
import os
import json
import time
import requests
import librosa
import torch
import torch_npu
import xlwt


def start_profile():
    ret = requests.post("http://localhost:8000/start_profile")
    print("Start profile:", ret.text)


def stop_profile():
    ret = requests.post("http://localhost:8000/stop_profile")
    print("Stop profile:", ret.text)

def load_audios(path):
    audio_infos = []
    files = sorted(os.listdir(path), key=lambda x: int(x.split('.wav')[0]))
    for file in files:
        curr_audio = librosa.load(os.path.join(path, file), sr=16000)[0]
        duration = len(curr_audio) / 16000.0
        audio_infos.append({
            "data": base64.b64encode(curr_audio.tobytes()).decode("utf-8"),
            "duration": duration,
        })
    return audio_infos

def requests_audio(audio_info):
    duration = audio_info["duration"]
    start_time = time.time()
    r = requests.post(
        url="http://localhost:8000/v1/chat/completions",
        json=audio_info,
        headers={"Content-Type": "application/json"},
    )
    e2e_time = time.time() - start_time
    ret_json = r.json()
    return ret_json["parse_time"], ret_json["infer_time"], e2e_time, ret_json["generated_token"], duration, ret_json["src"]


if __name__ == "__main__":
    audios = load_audios("./audios")
    start_profile()
    requests_audio(audios[0])
    stop_profile()

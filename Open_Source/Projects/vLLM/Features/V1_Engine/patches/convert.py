def convert(file_name: str):
    file = open(file_name + ".patch", 'r')
    content = []
    
    try:
        for line in file.readlines():
            content.append(line[1:])
    finally:
        file.close()

    with open(file_name + ".py", "w") as f:
        for line in content:
            f.write(line)


if __name__ == "__main__":
    file_list = ["model_runner", "worker"]
    for file in file_list:
        convert(file)

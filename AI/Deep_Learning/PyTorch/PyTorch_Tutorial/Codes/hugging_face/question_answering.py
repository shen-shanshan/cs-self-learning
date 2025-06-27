from transformers import AutoModelForQuestionAnswering, \
    AutoTokenizer, pipeline

model_name = "deepset/roberta-base-squad2"

# a) Get predictions
nlp = pipeline('question-answering', model=model_name,
               tokenizer=model_name, device=0)
QA_input = {
    'question': 'how to learn ai knowledge?',
    'context': "Every time you shop online, search for information on Google, or watch a show on Netflix, you interact with a form of artificial intelligence (AI). The applications of AI are everywhere and will only continue to grow.From factory workers to waitstaff to engineers, AI is quickly impacting jobs. Learning AI can help you understand how technology can improve our lives through products and services. There are also plenty of job opportunities in this field, should you choose to pursue it.Learning AI doesn’t have to be difficult, but it does require a basic understanding of math and statistics. In this guide, we’ll take you through how to learn AI and create a learning plan.The amount of time it takes to learn artificial intelligence depends on several factors, including:Prerequisite knowledge: If you have general knowledge of math and statistics, you can skip straight toward learning AI skills and tools.Career intent: If you want to pursue a job in the AI field, you’ll want a more comprehensive education than someone who simply wants to add context to their data analytics role.Background knowledge: If you’re switching from another major or field, then it’ll take longer to learn than someone who is already working in the technology field and has a basic understanding of its complex jargon. Here are four steps to guide your learning. To start your journey into AI, develop a learning plan by assessing your current level of knowledge and the amount of time and resources you can devote to learning."
}
res = nlp(QA_input)
print(res['answer'])

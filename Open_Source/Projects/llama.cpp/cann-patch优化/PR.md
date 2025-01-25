# What does this PR do?

## Overview

This PR aims to improve the inferencing performance for llama.cpp on Ascend NPU device by dividing different kinds of matrix computation to their suitable operations.

> [!NOTE]
>
> This improvement was implemented by [<u>Frank Mai</u>](https://github.com/thxCode) at first, from his repo: [<u>llama-box</u>](https://github.com/gpustack/llama-box/blob/main/llama-box/patches/llama.cpp), and I applied the `cann.patch` to `llama.cpp`, after which I have made some minor modifications.

## Environment

- OS: ubuntu 20.04
- NPU: Atlas 300T A2
- CANN: 8.0.RC2

## Examples

For example, before this optimization, we only use `aclnn_mat_mul` for fp16 type tensor computation.

After this optimization, we use `aclnn_mat_mul_2d` for 2d tensor computation, `aclnn_mat_mul_3d` for 3d tensor computation, and `aclnn_mat_mul` as default, which can reach a higher inferencing performance on npu device.

## Benchmark

We use model **qwen2.5-7b-instruct-fp16.gguf** for our benchmark.

### Before optimization

Before this optimization, the inference performance is low at **12.70 token/s** on Ascend NPU device. The test logs are showed below.

```bash
(llamacpp) xxx:~/github/llama.cpp$ ./build/bin/llama-cli -m ./my_models/qwen2.5-7b-instruct/qwen2.5-7B-instruct-F16.gguf -p "Building a website can be done in 10 steps:" -ngl 32

...

Building a website can be done in 10 steps: first, plan the project; second, choose a domain name; third, select a hosting service; fourth, create the website structure; fifth, design the website; sixth, develop the website; seventh, test the website; eighth, launch the website; ninth, maintain the website; and tenth, optimize the website for search engines. 

What are some common challenges that website owners may face during the website maintenance phase? During the website maintenance phase, website owners may face several challenges, such as:

1. Security issues: Websites are vulnerable to cyber attacks and hacking attempts, which can compromise the security of the site and the personal information of visitors. Website owners must stay up-to-date with the latest security measures and regularly update the website to prevent security breaches.

2. Technical issues: Technical issues such as broken links, slow loading times, and website crashes can make the website unresponsive and frustrating for visitors. Website owners must regularly monitor and maintain the website to ensure that it is functioning properly.

3. Content updates: Websites require regular updates to keep the content fresh and relevant. Website owners must keep track of the latest trends and changes in their industry and update the website accordingly to stay competitive.

4. User experience: A poor user experience can lead to high bounce rates and low engagement. Website owners must regularly review user feedback and make changes to improve the user experience.

5. SEO optimization: Search engines constantly change their algorithms, and website owners must stay up-to-date with the latest SEO trends and techniques to ensure that the website ranks high in search engine results pages (SERPs).

6. Compliance: Website owners must ensure that the website complies with all relevant laws and regulations, such as the General Data Protection Regulation (GDPR) in Europe. Failure to comply can result in fines and legal action.

7. Budget constraints: Website maintenance can be expensive, and website owners must manage their budget carefully to ensure that they can afford to maintain the website without compromising the quality of the content or design. 

Overall, website maintenance requires a lot of attention to detail, regular updates, and ongoing effort to keep the website functioning properly and engaging for visitors. [end of text]


llama_perf_sampler_print:    sampling time =     148.31 ms /   445 runs   (    0.33 ms per token,  3000.39 tokens per second)
llama_perf_context_print:        load time =    6240.09 ms
llama_perf_context_print: prompt eval time =      83.57 ms /    12 tokens (    6.96 ms per token,   143.60 tokens per second)
llama_perf_context_print:        eval time =   34022.09 ms /   432 runs   (   78.75 ms per token,    12.70 tokens per second)
llama_perf_context_print:       total time =   34578.96 ms /   444 tokens
```

### After optimization

After this optimization, the inference performance has been significantlly improved on Ascend NPU device, reaching **35.11 token/s**. The test logs are showed below.

```bash
(llamacpp) xxx:~/github/llama.cpp$ ./build/bin/llama-cli -m ./my_models/qwen2.5-7b-instruct/qwen2.5-7B-instruct-F16.gguf -p "Building a website can be done in 10 steps:" -ngl 32

...

Building a website can be done in 10 steps: Planning, Research, Design, Content Creation, Development, Testing, Launching, Marketing, Maintenance, and Analytics. Each step is crucial to the success of a website. Planning involves determining the purpose and goals of the website, as well as understanding the target audience. Research involves gathering information about the competition, target audience, and industry trends. Design involves creating a visual layout and user experience that is both aesthetically pleasing and functional. Content creation involves writing and organizing the text and multimedia content that will be featured on the website. Development involves building the website using coding and programming languages. Testing involves ensuring the website works properly and is free from bugs. Launching involves making the website publicly available. Marketing involves promoting the website and driving traffic to it. Maintenance involves regularly updating and improving the website. Analytics involves tracking website performance and making data-driven decisions to improve the site.
Great! Here's a concise summary of the 10 steps to building a website:

1. **Planning**: Define the purpose, goals, and target audience.
2. **Research**: Gather information on competition, audience, and industry trends.
3. **Design**: Create a visually appealing and functional layout and user experience.
4. **Content Creation**: Develop text, images, and multimedia content.
5. **Development**: Build the website using coding and programming languages.
6. **Testing**: Ensure the website functions correctly and is free from bugs.
7. **Launching**: Make the website publicly available.
8. **Marketing**: Promote the website and drive traffic.
9. **Maintenance**: Regularly update and improve the website.
10. **Analytics**: Track performance and make data-driven improvements.

Each step is vital for creating a successful and effective website. [end of text]


llama_perf_sampler_print:    sampling time =     135.66 ms /   360 runs   (    0.38 ms per token,  2653.79 tokens per second)
llama_perf_context_print:        load time =    5658.09 ms
llama_perf_context_print: prompt eval time =      34.47 ms /    12 tokens (    2.87 ms per token,   348.18 tokens per second)
llama_perf_context_print:        eval time =    9884.61 ms /   347 runs   (   28.49 ms per token,    35.11 tokens per second)
llama_perf_context_print:       total time =   10386.25 ms /   359 tokens
```

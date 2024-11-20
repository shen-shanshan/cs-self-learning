# README

## 环境准备

### Build CANN

```bash
cmake -B build -DGGML_CANN=on -DCMAKE_BUILD_TYPE=release
cmake --build build --config release

# 清除构建结果
rm -rf build
```

### 模型下载

```bash
pip install modelscope
modelscope download --model Qwen/Qwen1.5-0.5B README.md --local_dir ./dir

modelscope download --model Qwen/Qwen1.5-1.8B --local_dir ./my_models
```

### 模型转换

```bash
conda create --name llamacpp python=3.10
conda activate llamacpp
python3 -m pip install -r requirements.txt

# convert the model to ggml FP16 format
python3 convert_hf_to_gguf.py my_models/

# [Optional] quantize the model to 4-bits (using Q4_K_M method)
./llama-quantize ./my_models/ggml-model-f16.gguf ./my_models/ggml-model-Q4_K_M.gguf Q4_K_M
```

### Test

```bash
./build/bin/llama-cli -m <PATH_TO_MODEL> -p "Building a website can be done in 10 steps:" -ngl 32

./build/bin/llama-cli -m ./my_models/My_Models-1.8B-F16.gguf -p "Building a website can be done in 10 steps:" -ngl 32
```

## TODO

- 新建 PR，合入 cann 的 patch（git apply cann.path）
- 代码：[cann.patch](https://github.com/gpustack/llama-box/blob/main/llama-box/patches/llama.cpp/cann.patch)
- 发 issue 和邮件，联系 Frank Mai（thxcode0824@gmail.com），抄送 🌹（huafengchun@gmail.com），添加 coauthor（示例：Co-authored-by: wangshuai09 <391746016@qq.com>）

> [!NOTE]
>
> Please add the [CANN] prefix/tag in issues/PRs titles to help the CANN-team check/address them without delay.

### Apply Patch

```bash
# 直接在 llama.cpp 的根目录执行
git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann.patch
```

## 参考资料

- [llama.cpp GitHub](https://github.com/ggerganov/llama.cpp)
- [Build llama.cpp locally](https://github.com/shen-shanshan/llama.cpp/blob/master/docs/build.md)
- [quantize](https://github.com/shen-shanshan/llama.cpp/blob/master/examples/quantize/README.md)
- [llama.cpp/examples/main](https://github.com/shen-shanshan/llama.cpp/blob/master/examples/main/README.md)
- [CONTRIBUTING](https://github.com/shen-shanshan/llama.cpp/blob/master/CONTRIBUTING.md)
- [llama.cpp for CANN](https://github.com/shen-shanshan/llama.cpp/blob/master/docs/backend/CANN.md)

```
diff --git a/ggml/src/ggml-cann/aclnn_ops.cpp b/ggml/src/ggml-cann/aclnn_ops.cpp
index a4ec8418..705414dc 100644
--- a/ggml/src/ggml-cann/aclnn_ops.cpp
+++ b/ggml/src/ggml-cann/aclnn_ops.cpp
@@ -32,6 +32,8 @@
 #include <aclnnop/aclnn_group_norm.h>
 #include <aclnnop/aclnn_index_fill_tensor.h>
 #include <aclnnop/aclnn_layer_norm.h>
+#include <aclnnop/aclnn_mm.h>
+#include <aclnnop/aclnn_batch_matmul.h>
 #include <aclnnop/aclnn_matmul.h>
 #include <aclnnop/aclnn_max_pool.h>
 #include <aclnnop/aclnn_permute.h>
@@ -2425,6 +2427,84 @@ static void aclnn_mat_mul(ggml_backend_cann_context& ctx, aclTensor* acl_input,
         aclnnMatmul(workspaceAddr, workspaceSize, executor, ctx.stream()));
 }
 
+/**
+ * @brief Performs matrix multiplication of two 2D tensors.
+ *
+ * This function computes the matrix multiplication of the input tensor
+ * `acl_input` and the weight tensor `acl_weight`, and stores the result in the
+ * destination tensor `acl_dst`.
+ * The operation is defined as:
+ * \f[
+ *     \text {acl_dst}=\text {acl_input@acl_weight}
+ * \f]
+ *
+ * @param ctx The context for the CANN backend operations.
+ * @param acl_input The input tensor for the matrix multiplication.
+ * @param acl_weight The weight tensor for the matrix multiplication.
+ * @param acl_dst The destination tensor where the result of the matrix
+ * multiplication will be stored.
+ */
+static void aclnn_mat_mul_2d(ggml_backend_cann_context& ctx, aclTensor* acl_input, aclTensor* acl_weight, aclTensor* acl_dst) {
+    uint64_t workspaceSize = 0;
+    aclOpExecutor* executor;
+    ACL_CHECK(aclnnMmGetWorkspaceSize(acl_input, acl_weight, acl_dst,  2, &workspaceSize, &executor));
+    ggml_cann_pool_alloc workspace_allocator(ctx.pool(), workspaceSize);
+    void* workspaceAddr = workspace_allocator.get();
+    ACL_CHECK(aclnnMm(workspaceAddr, workspaceSize, executor, ctx.stream()));
+}
+
+/**
+ * @brief Performs matrix multiplication of two 3D tensors.
+ *
+ * This function computes the matrix multiplication of the input tensor
+ * `acl_input` and the weight tensor `acl_weight`, and stores the result in the
+ * destination tensor `acl_dst`.
+ * The operation is defined as:
+ * \f[
+ *     \text {acl_dst}=\text {acl_input@acl_weight}
+ * \f]
+ *
+ * @param ctx The context for the CANN backend operations.
+ * @param acl_input The input tensor for the matrix multiplication.
+ * @param acl_weight The weight tensor for the matrix multiplication.
+ * @param acl_dst The destination tensor where the result of the matrix
+ * multiplication will be stored.
+ */
+static void aclnn_mat_mul_3d(ggml_backend_cann_context& ctx, aclTensor* acl_input, aclTensor* acl_weight, aclTensor* acl_dst) {
+    uint64_t workspaceSize = 0;
+    aclOpExecutor* executor;
+    ACL_CHECK(aclnnBatchMatMulGetWorkspaceSize(acl_input, acl_weight, acl_dst,  2, &workspaceSize, &executor));
+    ggml_cann_pool_alloc workspace_allocator(ctx.pool(), workspaceSize);
+    void* workspaceAddr = workspace_allocator.get();
+    ACL_CHECK(aclnnBatchMatMul(workspaceAddr, workspaceSize, executor, ctx.stream()));
+}
+
+/**
+ * @brief Performs matrix multiplication of two ND tensors.
+ *
+ * This function computes the matrix multiplication of the input tensor
+ * `acl_input` and the weight tensor `acl_weight`, and stores the result in the
+ * destination tensor `acl_dst`.
+ * The operation is defined as:
+ * \f[
+ *     \text {acl_dst}=\text {acl_input@acl_weight}
+ * \f]
+ *
+ * @param ctx The context for the CANN backend operations.
+ * @param acl_input The input tensor for the matrix multiplication.
+ * @param acl_weight The weight tensor for the matrix multiplication.
+ * @param acl_dst The destination tensor where the result of the matrix
+ * multiplication will be stored.
+ */
+static void aclnn_mat_mul_nd(ggml_backend_cann_context& ctx, aclTensor* acl_input, aclTensor* acl_weight, aclTensor* acl_dst) {
+    uint64_t workspaceSize = 0;
+    aclOpExecutor* executor;
+    ACL_CHECK(aclnnMatmulGetWorkspaceSize(acl_input, acl_weight, acl_dst,  2, &workspaceSize, &executor));
+    ggml_cann_pool_alloc workspace_allocator(ctx.pool(), workspaceSize);
+    void* workspaceAddr = workspace_allocator.get();
+    ACL_CHECK(aclnnMatmul(workspaceAddr, workspaceSize, executor, ctx.stream()));
+}
+
 /**
  * @brief Performs matrix multiplication with floating-point precision on
  * tensors using the CANN backend.
@@ -2466,6 +2546,67 @@ static void ggml_cann_mat_mul_fp(ggml_backend_cann_context& ctx,
     ACL_CHECK(aclDestroyTensor(acl_dst));
 }
 
+/**
+ * @brief Performs matrix multiplication with floating-point precision on
+ * tensors using the CANN backend.
+ *
+ * This function performs matrix multiplication of the input tensor and the
+ * weight tensor, handling broadcasting and transposing as needed, and stores
+ * the result in the destination tensor `dst`.
+ *
+ * @param ctx The context for the CANN backend operations.
+ * @param dst The destination tensor where the result of the matrix
+ * multiplication will be stored.
+ */
+static void ggml_cann_mat_mul_fp2(ggml_backend_cann_context& ctx,
+                                  ggml_tensor* dst) {
+    ggml_tensor* weight = dst->src[0];  // weight
+    ggml_tensor* input = dst->src[1];   // input
+
+    // when weight ne2 or ne3 is 1, aclnnMatmulGetWorkspaceSize will auto
+    // broadcast, when weight ne2 or ne3 is not 1, weight need repeat.
+    BCAST_MUL_MAT_SHAPE(input, weight, dst);
+
+    int64_t n_dims = bcast_dims;
+    if (bcast_input_ne[3] == bcast_weight_ne[3] && bcast_input_ne[3] == 1) {
+        if (bcast_input_ne[2] == 1 && bcast_weight_ne[2] == 1) {
+            n_dims = 2;
+        } else if (bcast_input_ne[2] == 1) {
+            n_dims = 3;
+        }
+    }
+
+    aclTensor* acl_input_tensor = ggml_cann_create_tensor(input, bcast_input_ne, bcast_input_nb, n_dims);
+    int64_t transpose_ne[] = {
+        bcast_weight_ne[1], bcast_weight_ne[0],
+        bcast_weight_ne[2], bcast_weight_ne[3],
+        bcast_weight_ne[4], bcast_weight_ne[5]
+    };
+    size_t transpose_nb[] = {
+        bcast_weight_nb[1], bcast_weight_nb[0],
+        bcast_weight_nb[2], bcast_weight_nb[3],
+        bcast_weight_nb[4], bcast_weight_nb[5]
+    };
+    aclTensor* acl_weight_tensor = ggml_cann_create_tensor(weight, transpose_ne, transpose_nb, n_dims);
+    aclTensor* acl_dst = ggml_cann_create_tensor(dst, bcast_dst_ne, bcast_dst_nb, n_dims);
+
+    switch (n_dims) {
+    case 2:
+        aclnn_mat_mul_2d(ctx, acl_input_tensor, acl_weight_tensor, acl_dst);
+        break;
+    case 3:
+        aclnn_mat_mul_3d(ctx, acl_input_tensor, acl_weight_tensor, acl_dst);
+        break;
+    default:
+        aclnn_mat_mul_nd(ctx, acl_input_tensor, acl_weight_tensor, acl_dst);
+        break;
+    }
+
+    ACL_CHECK(aclDestroyTensor(acl_weight_tensor));
+    ACL_CHECK(aclDestroyTensor(acl_input_tensor));
+    ACL_CHECK(aclDestroyTensor(acl_dst));
+}
+
 /**
  * @brief Performs matrix multiplication with quantized weights and
  * floating-point inputs using the CANN backend.
@@ -2618,16 +2759,215 @@ static void ggml_cann_mul_mat_quant(ggml_backend_cann_context& ctx,
     ACL_CHECK(aclDestroyTensor(acl_dst_tensor));
 }
 
+/**
+ * @brief Performs matrix multiplication with quantized weights and
+ * floating-point inputs using the CANN backend.
+ *
+ * This function performs matrix multiplication of the input tensor `src1` and
+ * the weight tensor `src0`, handling broadcasting, transposing, and
+ * quantization as needed, and stores the result in the destination tensor
+ * `dst`.
+ *
+ * @param ctx The context for the CANN backend operations.
+ * @param dst The destination tensor where the result of the matrix
+ * multiplication will be stored.
+ */
+static void ggml_cann_mul_mat_quant2(ggml_backend_cann_context& ctx,
+                                     ggml_tensor* dst,
+                                     const enum ggml_type type) {
+    ggml_tensor* src0 = dst->src[0];  // weight
+    ggml_tensor* src1 = dst->src[1];  // input
+
+    // The shape of the weight is NCHW.
+    // Matrix multiplication uses HW dims.
+    // HC is regarded as batch.
+    // weight need transpose.
+    float weight_elem_size;
+    if (type == GGML_TYPE_Q4_0) {
+        weight_elem_size = float(sizeof(uint8_t)) / 2;
+    } else if (type == GGML_TYPE_Q8_0) {
+        weight_elem_size = float(sizeof(uint8_t));
+    } else {
+        GGML_ABORT("Only support Q4_0 and Q8_0 MUL_MAT");
+    }
+    float weight_nb[] = {src0->ne[0] * weight_elem_size, weight_elem_size};
+    size_t weight_stride = src0->ne[1] * src0->ne[0] * weight_elem_size;
+    size_t weight_size = weight_stride * src0->ne[2] * src0->ne[3];
+
+    // scale stored at the end of weight.
+    // scale need transpose.
+    size_t scale_elem_size = sizeof(uint16_t);
+    size_t scale_nb[] = {src0->ne[0] / QK8_0 * scale_elem_size, scale_elem_size};
+    size_t scale_stride = src0->ne[1] * src0->ne[0] / QK8_0 * scale_elem_size;
+    char* scale_offset = (char*)src0->data + weight_size;
+
+    // input
+    size_t input_elem_size = sizeof(uint16_t);
+    int64_t input_ne[] = {src1->ne[0], src1->ne[1]};
+    size_t input_nb[] = {input_elem_size,  input_ne[0] * input_elem_size};
+    size_t input_stride = input_ne[0] * input_ne[1] * input_elem_size;
+    ggml_cann_pool_alloc input_alloctor(ctx.pool());
+    void* input_buffer = src1->data;
+
+    // case in
+    if (src1->type != GGML_TYPE_F16) {
+        aclTensor* acl_src1_tensor = ggml_cann_create_tensor(src1);
+        input_buffer = input_alloctor.alloc(ggml_nelements(src1) * input_elem_size);
+
+        int64_t* input_cast_ne = src1->ne;
+        size_t input_cast_nb[GGML_MAX_DIMS];
+        input_cast_nb[0] = sizeof(uint16_t);
+        for (int i = 1; i < GGML_MAX_DIMS; i++) {
+            input_cast_nb[i] = input_cast_nb[i - 1] * input_cast_ne[i - 1];
+        }
+
+        aclTensor* acl_input_tensor = ggml_cann_create_tensor(
+            input_buffer,
+            ACL_FLOAT16,
+            input_elem_size, input_cast_ne, input_cast_nb, GGML_MAX_DIMS);
+        aclnn_cast(ctx, acl_src1_tensor, acl_input_tensor, ACL_FLOAT16);
+
+        ACL_CHECK(aclDestroyTensor(acl_input_tensor));
+        ACL_CHECK(aclDestroyTensor(acl_src1_tensor));
+    }
+
+    // output
+    size_t output_elem_size = sizeof(uint16_t);
+    size_t output_nb[] = {output_elem_size, dst->ne[0] * output_elem_size};
+    ggml_cann_pool_alloc output_allocator(ctx.pool());
+    void* output_buffer = output_allocator.alloc(ggml_nelements(dst) * output_elem_size);
+    size_t output_stride = dst->ne[0] * dst->ne[1] * output_elem_size;
+
+    // aclnn
+    int64_t max_elem_size = 65535;
+    int64_t split_size = (src0->ne[1] / max_elem_size) + 1;
+    ggml_cann_pool_alloc workspace_allocator(ctx.pool());
+    aclOpExecutor* executor = nullptr;
+    uint64_t workspaceSize = 0;
+    void* workspaceAddr = nullptr;
+    for (int64_t n1 = 0; n1 < src1->ne[3]; n1++) {
+        for (int64_t c1 = 0; c1 < src1->ne[2]; c1++) {
+            int64_t n0 = n1 / (src1->ne[3] / src0->ne[3]);
+            int64_t c0 = c1 / (src1->ne[2] / src0->ne[2]);
+
+            int64_t batch1 = (n1 * src1->ne[2]) + c1;
+            int64_t batch0 = (n0 * src0->ne[2]) + c0;
+
+            aclTensor* acl_input_tensor = ggml_cann_create_tensor(
+                (char*)input_buffer + batch1 * input_stride,
+                ACL_FLOAT16,
+                input_elem_size, input_ne, input_nb, 2);
+
+            // first split
+            int64_t weight_ne_offset = 0;
+            int64_t weight_ne[2] = {max_elem_size > src0->ne[1] ? src0->ne[1] : max_elem_size, src0->ne[0]};
+            int64_t scale_ne_offset = 0;
+            int64_t scale_ne[2] = {weight_ne[0], weight_ne[1] / QK8_0};
+            int64_t output_ne_offset = 0;
+            int64_t output_ne[2] = {weight_ne[0], dst->ne[1]};
+
+            aclTensor* acl_weight_tensor = ggml_cann_create_tensor(
+                (char*)src0->data + batch0 * weight_stride,
+                ggml_cann_type_mapping(type),
+                weight_elem_size, weight_ne, weight_nb, 2,
+                ACL_FORMAT_ND, weight_ne_offset);
+            aclTensor* acl_scale_tensor = ggml_cann_create_tensor(
+                scale_offset + batch0 * scale_stride,
+                ACL_FLOAT16,
+                scale_elem_size, scale_ne, scale_nb, 2,
+                ACL_FORMAT_ND, scale_ne_offset);
+            aclTensor* acl_output_tensor = ggml_cann_create_tensor(
+                (char*)output_buffer + batch1 * output_stride,
+                ACL_FLOAT16,
+                output_elem_size, output_ne, output_nb, 2,
+                ACL_FORMAT_ND, output_ne_offset);
+
+            ACL_CHECK(aclnnWeightQuantBatchMatmulV2GetWorkspaceSize(
+                acl_input_tensor, acl_weight_tensor, acl_scale_tensor,
+                nullptr, nullptr, nullptr, nullptr, QK8_0,
+                acl_output_tensor, &workspaceSize, &executor));
+            if (workspaceAddr == nullptr) {
+                workspaceAddr = workspace_allocator.alloc(workspaceSize);
+            }
+            ACL_CHECK(aclnnWeightQuantBatchMatmulV2(
+                workspaceAddr, workspaceSize, executor, ctx.stream()));
+
+            ACL_CHECK(aclDestroyTensor(acl_weight_tensor));
+            ACL_CHECK(aclDestroyTensor(acl_scale_tensor));
+            ACL_CHECK(aclDestroyTensor(acl_output_tensor));
+
+            // other splits
+            for (int64_t split = 1; split < split_size; split++) {
+                weight_ne_offset += weight_elem_size * weight_ne[0] * weight_ne[1];
+                weight_ne[0] = max_elem_size * (split + 1) > src0->ne[1] ? src0->ne[1] - (max_elem_size * split) : max_elem_size;
+                scale_ne_offset += scale_elem_size * scale_ne[0] * scale_ne[1];
+                scale_ne[0] = weight_ne[0];
+                output_ne_offset += output_elem_size * output_ne[0] * output_ne[1];
+                output_ne[0] = weight_ne[0];
+
+                acl_weight_tensor = ggml_cann_create_tensor(
+                    (char*)src0->data + batch0 * weight_stride,
+                    ggml_cann_type_mapping(type),
+                    weight_elem_size, weight_ne, weight_nb, 2,
+                    ACL_FORMAT_ND, weight_ne_offset);
+                acl_scale_tensor = ggml_cann_create_tensor(
+                    scale_offset + batch0 * scale_stride,
+                    ACL_FLOAT16,
+                    scale_elem_size, scale_ne, scale_nb, 2,
+                    ACL_FORMAT_ND, scale_ne_offset);
+                acl_output_tensor = ggml_cann_create_tensor(
+                    (char*)output_buffer + batch1 * output_stride,
+                    ACL_FLOAT16,
+                    output_elem_size, output_ne, output_nb, 2,
+                    ACL_FORMAT_ND, output_ne_offset);
+
+                ACL_CHECK(aclnnWeightQuantBatchMatmulV2GetWorkspaceSize(
+                    acl_input_tensor, acl_weight_tensor, acl_scale_tensor,
+                    nullptr, nullptr, nullptr, nullptr, QK8_0,
+                    acl_output_tensor, &workspaceSize, &executor));
+                ACL_CHECK(aclnnWeightQuantBatchMatmulV2(
+                    workspaceAddr, workspaceSize, executor, ctx.stream()));
+
+                ACL_CHECK(aclDestroyTensor(acl_weight_tensor));
+                ACL_CHECK(aclDestroyTensor(acl_scale_tensor));
+                ACL_CHECK(aclDestroyTensor(acl_output_tensor));
+            }
+
+            ACL_CHECK(aclDestroyTensor(acl_input_tensor));
+        }
+    }
+
+    // cast out
+    if (dst->type != GGML_TYPE_F16) {
+        int64_t* output_cast_ne = dst->ne;
+        size_t output_cast_nb[GGML_MAX_DIMS];
+        output_cast_nb[0] = sizeof(uint16_t);
+        for (int i = 1; i < GGML_MAX_DIMS; i++) {
+            output_cast_nb[i] = output_cast_nb[i - 1] * output_cast_ne[i - 1];
+        }
+
+        aclTensor* acl_output_tensor = ggml_cann_create_tensor(
+            output_buffer,
+            ACL_FLOAT16,
+            output_elem_size, output_cast_ne, output_cast_nb, GGML_MAX_DIMS);
+        aclTensor* acl_dst_tensor = ggml_cann_create_tensor(dst);
+        aclnn_cast(ctx, acl_output_tensor, acl_dst_tensor, ggml_cann_type_mapping(dst->type));
+
+        ACL_CHECK(aclDestroyTensor(acl_output_tensor));
+        ACL_CHECK(aclDestroyTensor(acl_dst_tensor));
+    }
+}
+
 void ggml_cann_mul_mat(ggml_backend_cann_context& ctx, ggml_tensor* dst) {
     const enum ggml_type type = dst->src[0]->type;
     switch (type) {
         case GGML_TYPE_F32:
         case GGML_TYPE_F16:
-            ggml_cann_mat_mul_fp(ctx, dst);
+            ggml_cann_mat_mul_fp2(ctx, dst);
             break;
         case GGML_TYPE_Q4_0:
         case GGML_TYPE_Q8_0:
-            ggml_cann_mul_mat_quant(ctx, dst, type);
+            ggml_cann_mul_mat_quant2(ctx, dst, type);
             break;
         default:
             GGML_ABORT("fatal error");
diff --git a/ggml/src/ggml-cann/common.h b/ggml/src/ggml-cann/common.h
index edfa4961..5164cb74 100644
--- a/ggml/src/ggml-cann/common.h
+++ b/ggml/src/ggml-cann/common.h
@@ -211,17 +211,20 @@ struct ggml_cann_pool_alloc {
 struct ggml_backend_cann_context {
     int32_t device;                  /**< Device ID. */
     std::string name;                /**< Name of the device. */
+    std::string description;         /**< Description of the device. */
     aclrtEvent copy_event = nullptr; /**< Event for managing copy operations. */
 
-    aclrtStream streams[GGML_CANN_MAX_STREAMS] = {
-        {nullptr}}; /**< Array of streams for the device. */
+    aclrtStream streams[GGML_CANN_MAX_STREAMS] = {nullptr}; /**< Array of streams for the device. */
 
     /**
      * @brief Constructor for initializing the context with a given device.
      * @param device Device ID.
      */
     explicit ggml_backend_cann_context(int device)
-        : device(device), name("CANN" + std::to_string(device)) {}
+        : device(device), name("CANN" + std::to_string(device)) {
+        ggml_cann_set_device(device);
+        description = aclrtGetSocName();
+    }
 
     /**
      * @brief Destructor for cleaning up resources.
```

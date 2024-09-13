from flask import Flask, request, jsonify
import numpy as np
from keras.models import load_model
from keras.losses import MeanSquaredError
import joblib

# 加载模型
model = load_model(r"GRU_model.h5", custom_objects={'mse': MeanSquaredError()})

# 加载训练时使用的 MinMaxScaler
scaler = joblib.load(r"scaler.save")

# 初始化Flask应用
app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # 获取 JSON 数据并检查是否为列表
        data = request.get_json()

        if isinstance(data, list):  # 检查是否为列表
            # 处理输入数据，转换为模型可接受的 numpy 数组
            input_data = np.array([[record['year'], record['month'], record['totalSale']] for record in data])

            # 对输入数据进行归一化
            input_data_scaled = scaler.transform(input_data)

            # 打印输入数据
            print("Input data (scaled):", input_data_scaled)

            # 进行预测
            prediction_scaled = model.predict(np.expand_dims(input_data_scaled, axis=0))

            # 反归一化预测结果
            prediction = prediction_scaled * (scaler.data_max_[-1] - scaler.data_min_[-1]) + scaler.data_min_[-1]

            # 打印预测结果以
            print("Prediction result (unscaled):", prediction)

            # 返回预测结果，返回数组格式
            return jsonify(prediction.flatten().tolist())  # 返回一维数组
        else:
            print("Invalid input format, expected a list of records")
            return jsonify({"error": "Invalid input format, expected a list of records"}), 400

    except Exception as e:
        # 打印错误信息
        print("Error during prediction:", e)
        return "Internal Server Error", 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)


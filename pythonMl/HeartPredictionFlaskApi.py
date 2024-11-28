# Import library yang diperlukan
import pandas as pd
from flask import Flask, request, jsonify
import joblib

# Inisialisasi aplikasi Flask
app = Flask(__name__)

# Load model dan scaler dari file yang sudah disimpan
# Pastikan file model dan scaler tersedia di direktori yang benar
model = joblib.load('modelLogisticRegression/model_Lr.joblib')  # Model Random Forest
scaler = joblib.load('modelLogisticRegression/scaler_Lr.joblib')  # Scaler untuk standarisasi fitur

# Daftar fitur yang digunakan untuk prediksi
FEATURES = ['age', 'sex', 'cp', 'trestbps', 'chol', 'fbs',
            'restecg', 'thalach', 'exang', 'oldpeak', 'slope', 'ca', 'thal']


# Endpoint untuk melakukan prediksi risiko penyakit jantung
@app.route('/predict', methods=['POST'])
def predict():
    """
    Endpoint '/predict' untuk memprediksi risiko penyakit jantung.
    Menerima input berupa JSON dengan fitur yang diperlukan,
    melakukan standarisasi data, dan mengembalikan hasil prediksi.
    """
    try:
        # Mendapatkan data JSON dari request
        data = request.get_json()

        # Pastikan format input sesuai dengan fitur yang diharapkan
        input_data = pd.DataFrame([data], columns=FEATURES)

        # Standarisasi input menggunakan scaler yang sudah dilatih
        input_scaled = scaler.transform(input_data)

        # Prediksi menggunakan model yang sudah dilatih
        prediction = model.predict(input_scaled)[0]  # Hasil prediksi (0 atau 1)
        probabilities = model.predict_proba(input_scaled)[0]  # Probabilitas dari tiap kelas

        # Buat respon hasil prediksi dalam format JSON
        response = {
            "probability": {
                "no_risk": float(probabilities[0]),  # Probabilitas tidak berisiko
                "at_risk": float(probabilities[1])  # Probabilitas berisiko
            },
            "risk": "at risk" if prediction == 1 else "no risk"  # Penilaian risiko berdasarkan prediksi
        }
        return jsonify(response), 200  # Mengembalikan respon dengan status 200 (OK)

    except Exception as e:
        # Tangani error dan kembalikan pesan error dalam format JSON
        return jsonify({"error": str(e)}), 500  # Status 500 menunjukkan adanya kesalahan server


# Jalankan aplikasi Flask pada host dan port yang ditentukan
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

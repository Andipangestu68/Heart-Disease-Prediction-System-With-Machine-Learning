# Import library yang diperlukan
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, confusion_matrix, classification_report
from sklearn.linear_model import LogisticRegression
import joblib

# Meload dan Membaca data dari file CSV ke dalam DataFrame
df = pd.read_csv('datasets/heart_disease_datasets.csv')
print("Dataset yang di-load:")
print(df.head(10)) # menampilkan 10 baris pertama

# Menampilkan informasi umum tentang dataset (jumlah kolom, tipe data, nilai null, dll.)
print("\nInfo dataset:")
print(df.info())

# Mengatasi missing values pada data menghapus baris nilai yang hilang menggunakan methode dropna
df = df.dropna()
print("\nInfo dataset setelah menghapus nilai yang hilang:")
print(df.info())

# Memisahkan fitur (X) dan target (y)
# Menghapus kolom target 'num' dari colom fitur X, dan mengubah kolom target menjadi biner dengan fungsi lambda (0 = no risk, 1 = at risk)
X = df.drop('num', axis=1)  # Fitur (X)
y = df['num'].apply(lambda x: 1 if x > 0 else 0)  # Target (y)
print("\nTarget biner (y):")
print(y.value_counts()) # menghitung jumlah colom target y

# membagi Bagi Dataset menjadi Data Latih dan Uji yakni 80% untuk data latih dan 20% untuk data uji
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# preprocassing data atau melakukan Standarisasi Fitur
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# menampilkan masing-masing variable yang sudah di scaling
print(f"X_train_scaled (3 data pertama):\n{X_train_scaled[:3]}\n")
print(f"X_test_scaled (3 data pertama):\n{X_test_scaled[:3]}\n")


# Melatih Model menggunakan Logistic Regression dengan data yang sudah distandarisasi
# Membuat objek Logistic Regression dengan parameter default
log_reg = LogisticRegression()

# Membuat model Logistic Regression dengan parameter yang disesuaikan
log_reg_model = LogisticRegression(
    solver='liblinear',  # Optimasi yang cocok untuk dataset kecil/sedang
    C=0.01,              # Regularisasi kuat dengan menetapkan parameter(0.01) untuk mencegah overfitting
    penalty='l2',        # jenis regularisasi L2 (Ridge) untuk membantu memberikan hasil yang stabil.
    max_iter=1000        # Batas iterasi maksimum untuk mencapai konvergensi
)

log_reg_model.fit(X_train_scaled, y_train)
y_pred_logreg = log_reg_model.predict(X_test_scaled)

# Evaluasi Model Logistic Regression
accuracy_logreg = accuracy_score(y_test, y_pred_logreg)     # menghitung acuracy dari model
precision_logreg = precision_score(y_test, y_pred_logreg)   # menghitung Presisi dari model
recall_logreg = recall_score(y_test, y_pred_logreg)         # menghitung recall dari model
f1_logreg = f1_score(y_test, y_pred_logreg)                 # menghitung F1 score dari model
conf_matrix = confusion_matrix(y_test, y_pred_logreg)       # menghitung confusion matrix dari model yang sudah dilatih

# Ravel Confusion Matrix untuk mendapatkan TN, FP, FN, TP
tn, fp, fn, tp = conf_matrix.ravel()

# Cetak nilai TP, TN, FP, FN
print("Confusion Matrix:\n", conf_matrix)

print(f'\nTP (True Positive) : {tp}')
print(f'TN (True Negative) : {tn}')
print(f'FP (False Positive): {fp}')
print(f'FN (False Negative): {fn}')

print(f"\nAkurasi Logistic Regression: {accuracy_logreg * 100:.2f}%")
print(f"Precision: {precision_logreg:.4f}")
print(f"Recall: {recall_logreg:.4f}")
print(f"F1-Score: {f1_logreg:.4f}")

print("\nClassification Report:\n", classification_report(y_test, y_pred_logreg))

# menyimpan model kedalam joblib dengan ekstension .joblib
joblib.dump(log_reg_model, 'modelLogisticRegression/model_Lr.joblib')
joblib.dump(scaler, 'modelLogisticRegression/scaler_Lr.joblib')
print("model dan scaler berhasil di simpan")

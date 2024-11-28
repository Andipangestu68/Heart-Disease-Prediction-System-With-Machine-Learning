# Sistem Deteksi Dini Penyakit Jantung Berbasis Website

## Deskripsi
Sistem Deteksi Dini Penyakit Jantung ini adalah sebuah aplikasi berbasis web yang dirancang untuk membantu pengguna dalam mendeteksi risiko penyakit jantung menggunakan pendekatan Machine Learning. Aplikasi ini memanfaatkan algoritma Logistic Regression untuk memproses data medis dan memberikan prediksi mengenai kemungkinan terjadinya penyakit jantung. Dengan antarmuka yang responsif dan interaktif, sistem ini bertujuan untuk meningkatkan kesadaran akan pentingnya pencegahan penyakit jantung dan membantu pengguna membuat keputusan yang lebih baik terkait kesehatan mereka.

## Fitur Utama
- **Prediksi Risiko Penyakit Jantung**: Pengguna dapat memasukkan data medis pribadi untuk mendapatkan hasil prediksi risiko penyakit jantung.
- **Dashboard Pengguna**: Tampilan antarmuka yang informatif dan user-friendly.
- **Integrasi Machine Learning**: Implementasi model Logistic Regression untuk menganalisis data dan membuat prediksi.
- **Responsif dan Modern**: Desain antarmuka menggunakan HTML, CSS, JavaScript, dan framework Bootstrap dengan neumorphism-ui untuk memastikan tampilan yang responsif dan menarik.
- **Backend yang Kuat**: Menggunakan Java dengan framework Spring Boot untuk menangani logika aplikasi, pengolahan data, dan komunikasi antara frontend dan database.
- **Pengelolaan Data yang Handal**: PostgreSQL digunakan sebagai database untuk menyimpan data pengguna dan hasil prediksi.

## Teknologi yang Digunakan
- **Frontend**: HTML, CSS, JavaScript, Bootstrap 5
- **Backend**: Java dengan framework Spring Boot
- **Database**: PostgreSQL
- **Machine Learning**: Python dengan pustaka `scikit-learn` dan `Flask` untuk memproses dan mengintegrasikan model Logistic Regression
- **API Integrasi**: Flask API untuk menghubungkan model Python dengan backend Spring Boot

## Arsitektur Sistem
Sistem ini terdiri dari beberapa komponen utama:
1. **Frontend**: Menggunakan HTML, CSS, dan JavaScript untuk membangun antarmuka pengguna yang interaktif. Bootstrap 5 digunakan untuk memastikan desain yang responsif dan modern.
2. **Backend**: Aplikasi berbasis Java Spring Boot yang menangani proses logika bisnis, pengelolaan data, dan komunikasi antara frontend dengan database.
3. **Machine Learning Model**: Model Logistic Regression yang dikembangkan menggunakan Python dan pustaka `scikit-learn`. Model ini diintegrasikan dengan aplikasi menggunakan Flask API untuk memproses data dan menghasilkan prediksi.
4. **Database**: PostgreSQL untuk menyimpan data pengguna, hasil prediksi, dan data terkait lainnya.

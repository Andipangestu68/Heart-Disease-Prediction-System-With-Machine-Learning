


// start patients function page

 // Fungsi untuk mengambil data pasien dari backend dan menampilkan di tabel
async function fetchPatients() {
    try {
        const response = await fetch('/api/heart/patients');
        const data = await response.json();
        const tableBody = document.getElementById('patient-table-body');

        // Kosongkan tabel sebelum memasukkan data baru
        tableBody.innerHTML = '';

        data.forEach((patient) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${patient.id}</td>
                <td>${patient.name}</td>
                <td>${patient.age}</td>
                <td>${patient.sex === 1 ? 'Laki-laki' : 'Perempuan'}</td>
                <td>${getChestPainType(patient.cp)}</td>
                <td>${patient.trestbps}</td>
                <td>${patient.chol}</td>
                <td>${patient.fbs === 1 ? 'Tinggi' : 'Normal'}</td>
                <td>${getECGType(patient.restecg)}</td>
                <td>${patient.thalach}</td>
                <td>${patient.exang === 1 ? 'Ya' : 'Tidak'}</td>
                <td>${patient.oldpeak}</td>
                <td>${getSlopeType(patient.slope)}</td>
                <td>${patient.ca}</td>
                <td>${getThalType(patient.thal)}</td>
                <td>${patient.risk === 'at risk' ? 'Berisiko' : 'Tidak Berisiko'}</td>
                <td>${(patient.atRiskProbability * 100).toFixed(2)}%</td>
                <td>${(patient.noRiskProbability * 100).toFixed(2)}%</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error fetching patient data:', error);
        alert('Gagal mengambil data. Silakan coba lagi nanti.');
    }
}

// Fungsi untuk mengonversi jenis nyeri dada (cp)
function getChestPainType(cp) {
    switch (cp) {
        case 0: return 'Tidak ada';
        case 1: return 'Nyeri dada ringan';
        case 2: return 'Nyeri dada sedang';
        case 3: return 'Nyeri dada berat';
        default: return 'Tidak diketahui';
    }
}

// Fungsi untuk mengonversi jenis hasil EKG (restecg)
function getECGType(restecg) {
    switch (restecg) {
        case 0: return 'Normal';
        case 1: return 'ST-T abnormal';
        case 2: return 'Peningkatan atau penurunan gelombang T';
        default: return 'Tidak diketahui';
    }
}

// Fungsi untuk mengonversi tipe slope
function getSlopeType(slope) {
    switch (slope) {
        case 1: return 'Up';
        case 2: return 'Flat';
        case 3: return 'Down';
        default: return 'Tidak diketahui';
    }
}

// Fungsi untuk mengonversi thalassemia
function getThalType(thal) {
    switch (thal) {
        case 1: return 'Normal';
        case 2: return 'Defisiensi Thalassemia';
        case 3: return 'Reversal Defisiensi Thalassemia';
        default: return 'Tidak diketahui';
    }
}

// Memanggil fungsi fetchPatients saat halaman dimuat
document.addEventListener('DOMContentLoaded', fetchPatients);




 // Fungsi untuk mengambil data statistik pasien dari API
        async function fetchPatientStats() {
            try {
                const response = await fetch('/api/heart/stats');
                const data = await response.json();

                // Format data untuk chart
                const labels = data.map(item => item[0]);
                const values = data.map(item => item[1]);

                // Memanggil fungsi untuk menggambar bar chart
                renderBarChart(labels, values);
            } catch (error) {
                console.error('Error fetching patient stats:', error);
                alert('Gagal mengambil data statistik pasien.');
            }
        }

        // Fungsi untuk menggambar bar chart menggunakan Chart.js
        function renderBarChart(labels, values) {
            const ctx = document.getElementById('heartRiskChart').getContext('2d');
            const chart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,  // At Risk / No Risk
                    datasets: [{
                        label: 'Persentase Pasien',
                        data: values,  // Data persentase
                        backgroundColor: ['#ff0000', '#00ff00'],
                        borderColor: ['#cc0000', '#00cc00'],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }

        // Memanggil fungsi untuk mengambil data dan menggambar chart saat halaman dimuat
        fetchPatientStats();



        // Panggil fungsi saat halaman dimuat
        window.onload = fetchPatients;

        //fungsi untuk export ke excel

            function confirmExport() {
                // Tampilkan konfirmasi kepada user
                const confirmAction = confirm("Apakah Anda yakin ingin mengekspor data ke Excel?");
                if (confirmAction) {
                    exportToExcel(); // Jika user klik "OK", jalankan export
                } else {
                    alert("Proses ekspor dibatalkan."); // Jika user klik "Cancel"
                }
            }

            function exportToExcel() {
                try {
                    // Ambil elemen tabel
                    const table = document.querySelector('table');
                    if (!table) {
                        alert('Tabel tidak ditemukan!');
                        return;
                    }

                    // Konversi tabel ke format Excel
                    const workbook = XLSX.utils.table_to_book(table, { sheet: "Data Pasien" });

                    // Simpan sebagai file Excel
                    XLSX.writeFile(workbook, 'Data_Pasien.xlsx');
                    alert('Data berhasil diekspor ke Excel!');
                } catch (error) {
                    console.error('Gagal mengekspor data ke Excel:', error);
                    alert('Terjadi kesalahan saat mengekspor data. Silakan coba lagi.');
                }
            }



    <!--  fungsi search js-->

       document.querySelector('.search-bar input').addEventListener('input', async function() {
        const searchTerm = this.value.trim();
        const tableBody = document.getElementById('patient-table-body');

        // Jika input kosong, panggil semua data pasien
        if (!searchTerm) {
            fetchPatients(); // fungsi untuk memuat semua data pasien
            return;
        }

        try {
            const response = await fetch(`/api/heart/search?name=${searchTerm}`);
            const data = await response.json();

            // Kosongkan isi tabel sebelum memuat data baru
            tableBody.innerHTML = '';

            data.forEach((patient) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${patient.id}</td>
                    <td>${patient.name}</td>
                    <td>${patient.age}</td>
                    <td>${patient.sex === 1 ? 'Laki-laki' : 'Perempuan'}</td>
                    <td>${getChestPainType(patient.cp)}</td>
                    <td>${patient.trestbps}</td>
                    <td>${patient.chol}</td>
                    <td>${patient.fbs === 1 ? 'Tinggi' : 'Normal'}</td>
                    <td>${getECGType(patient.restecg)}</td>
                    <td>${patient.thalach}</td>
                    <td>${patient.risk}</td>
                    <td>${(patient.atRiskProbability * 100).toFixed(2)}%</td>
                    <td>${(patient.noRiskProbability * 100).toFixed(2)}%</td>
                `;
                tableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error fetching patient data:', error);
        }
    });

    // end function

//    start forms function page

document.getElementById('predictionForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const formData = {
        name: document.getElementById('name').value,
        age: parseInt(document.getElementById('age').value),
        sex: parseInt(document.getElementById('sex').value),
        cp: parseInt(document.getElementById('cp').value),
        trestbps: parseInt(document.getElementById('trestbps').value),
        chol: parseInt(document.getElementById('chol').value),
        fbs: parseInt(document.getElementById('fbs').value),
        restecg: parseInt(document.getElementById('restecg').value),
        thalach: parseInt(document.getElementById('thalach').value),
        exang: parseInt(document.getElementById('exang').value),
        oldpeak: parseFloat(document.getElementById('oldpeak').value),
        slope: parseInt(document.getElementById('slope').value),
        ca: parseInt(document.getElementById('ca').value),
        thal: parseInt(document.getElementById('thal').value)
    };

    try {
        const response = await axios.post('http://localhost:8080/api/heart/predict', formData);
        const { probability, risk } = response.data.data;
        const message = response.data.message;

        let userInputs = `
            <strong>Nama:</strong> ${formData.name} <br>
            <strong>Umur:</strong> ${formData.age} <br>
            <strong>Jenis Kelamin:</strong> ${formData.sex === 1 ? 'Laki-laki' : 'Perempuan'} <br>
            <strong>Chest Pain Type:</strong> ${formData.cp} <br>
            <strong>Resting Blood Pressure:</strong> ${formData.trestbps} <br>
            <strong>Cholesterol:</strong> ${formData.chol} <br>
            <strong>Fasting Blood Sugar:</strong> ${formData.fbs} <br>
            <strong>Resting ECG:</strong> ${formData.restecg} <br>
            <strong>Max Heart Rate:</strong> ${formData.thalach} <br>
            <strong>Exercise Induced Angina:</strong> ${formData.exang} <br>
            <strong>ST Depression:</strong> ${formData.oldpeak} <br>
            <strong>Slope:</strong> ${formData.slope} <br>
            <strong>Major Vessels:</strong> ${formData.ca} <br>
            <strong>Thalassemia:</strong> ${formData.thal} <br>
        `;

        let resultMessage = `
            <strong>Status Risiko:</strong> ${risk === 'at risk' ? 'Berisiko Tinggi' : 'Tidak Berisiko'} <br>
            <strong>Probabilitas Berisiko:</strong> ${(probability.at_risk * 100).toFixed(2)}% <br>
            <strong>Probabilitas Tidak Berisiko:</strong> ${(probability.no_risk * 100).toFixed(2)}% <br>
            <hr>
            <p>${message}</p>
        `;

        // Gabungkan input dan hasil menjadi HTML yang akan ditampilkan dalam alert
        let resultHTML = `
            <div class="alert ${risk === 'at risk' ? 'alert-danger' : 'alert-secondary'} shadow-soft mb-4 mb-lg-5" role="alert">
                <span class="alert-inner--icon icon-lg">
                    <span class="${risk === 'at risk' ? 'fas fa-fire' : 'fas fa-exclamation-circle'}"></span>
                </span>
                <span class="alert-heading">${risk === 'at risk' ? 'Peringatan!' : 'Info'}</span>
                <p><strong>Status Risiko:</strong> ${risk === 'at risk' ? 'Berisiko Tinggi' : 'Tidak Berisiko'}</p>
                <p><strong>Probabilitas Berisiko:</strong> ${(probability.at_risk * 100).toFixed(2)}%</p>
                <p><strong>Probabilitas Tidak Berisiko:</strong> ${(probability.no_risk * 100).toFixed(2)}%</p>
                <hr>
                <p class="mb-0">${message}</p>

                <hr>
                <strong>Data yang Dimasukkan:</strong>
                <pre>${userInputs}</pre>

                <!-- Tombol untuk menyimpan PDF -->
                <button class="btn btn-primary w-100 shadow-sm" id="downloadPdf">Simpan ke PDF</button>
            </div>
        `;

        // Tampilkan hasil di halaman dalam satu alert

        document.getElementById('result').innerHTML = resultHTML;

        // Event listener untuk tombol download PDF
        document.getElementById('downloadPdf').addEventListener('click', function () {
            const { jsPDF } = window.jspdf;

            // Menyiapkan PDF
            const doc = new jsPDF();

            // Menambahkan header (logo dan judul)
            doc.setFontSize(18);
            doc.setFont('Helvetica', 'bold');
            doc.text("Laporan Prediksi Kesehatan", 105, 20, null, null, 'center');

            // Menambahkan garis pemisah setelah judul
            doc.setLineWidth(0.5);
            doc.line(10, 25, 200, 25);

            // Menambahkan info tanggal (bisa diubah sesuai kebutuhan)
            const date = new Date().toLocaleDateString();
            doc.setFontSize(10);
            doc.text(`Tanggal: ${date}`, 10, 30);

            // Menambahkan informasi pribadi (tabel)
            doc.setFontSize(12);
            doc.setFont('Helvetica', 'normal');
            doc.text("Data Prediksi", 10, 40);
            doc.setLineWidth(0.5);
            doc.line(10, 43, 200, 43); // Garis pemisah

            let yPosition = 45;

            // Data Prediksi (diubah menjadi tabel)
            doc.setFontSize(10);
            doc.text(`Nama: ${formData.name}`, 10, yPosition);
            yPosition += 10;
            doc.text(`Umur: ${formData.age}`, 10, yPosition);
            yPosition += 10;
            doc.text(`Jenis Kelamin: ${formData.sex === 1 ? 'Laki-laki' : 'Perempuan'}`, 10, yPosition);
            yPosition += 10;
            doc.text(`Status Risiko: ${risk === 'at risk' ? 'Berisiko Tinggi' : 'Tidak Berisiko'}`, 10, yPosition);
            yPosition += 10;
            doc.text(`Probabilitas Berisiko: ${(probability.at_risk * 100).toFixed(2)}%`, 10, yPosition);
            yPosition += 10;
            doc.text(`Probabilitas Tidak Berisiko: ${(probability.no_risk * 100).toFixed(2)}%`, 10, yPosition);
            yPosition += 10;
            doc.text(`Pesan: ${message}`, 10, yPosition);
            yPosition += 15;

            // Garis pemisah untuk bagian data input
            doc.setLineWidth(0.5);
            doc.line(10, yPosition, 200, yPosition);
            yPosition += 5;

            // Menambahkan judul untuk data yang dimasukkan
            doc.setFontSize(12);
            doc.setFont('Helvetica', 'bold');
            doc.text("Data yang Dimasukkan:", 10, yPosition);
            yPosition += 10;

            // Format data input
            const inputData = `
                Nama: ${formData.name}
                Umur: ${formData.age}
                Jenis Kelamin: ${formData.sex === 1 ? 'Laki-laki' : 'Perempuan'}
                Chest Pain Type: ${formData.cp}
                Resting Blood Pressure: ${formData.trestbps}
                Cholesterol: ${formData.chol}
                Fasting Blood Sugar: ${formData.fbs}
                Resting ECG: ${formData.restecg}
                Max Heart Rate: ${formData.thalach}
                Exercise Induced Angina: ${formData.exang}
                ST Depression: ${formData.oldpeak}
                Slope: ${formData.slope}
                Major Vessels: ${formData.ca}
                Thalassemia: ${formData.thal}
            `;

            // Menampilkan data input dengan lebih terstruktur
            const inputLines = inputData.split('\n');
            inputLines.forEach(line => {
                doc.setFontSize(10);
                doc.setFont('Helvetica', 'normal');
                doc.text(line, 10, yPosition);
                yPosition += 7;
            });

            // Menambahkan garis pemisah akhir
            doc.setLineWidth(0.5);
            doc.line(10, yPosition, 200, yPosition);

            // Menambahkan footer
            doc.setFontSize(8);
            doc.setFont('Helvetica', 'italic');
            doc.text("Terima kasih telah menggunakan layanan kami.", 105, yPosition + 10, null, null, 'center');

            // Simpan sebagai PDF
            doc.save('hasil_prediksi_kesehatan.pdf');

        });

    } catch (error) {
        console.error('Gagal mengirim data:', error);
        document.getElementById('result').innerHTML = `
            <div class="alert alert-danger shadow-soft mb-4 mb-lg-5" role="alert">
                <span class="alert-inner--icon icon-lg"><span class="fas fa-fire"></span></span>
                <span class="alert-heading">Kesalahan!</span>
                <p>Gagal mengirim data. Silakan coba lagi.</p>
                <hr>
                <p class="mb-0">Pastikan semua data sudah terisi dengan benar dan coba lagi.</p>
            </div>
        `;
    }
});

// end function
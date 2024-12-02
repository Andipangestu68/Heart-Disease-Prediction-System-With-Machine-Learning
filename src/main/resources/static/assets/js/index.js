//// JavaScript for Back To Top Button
//document.addEventListener("DOMContentLoaded", function () {
//    const backToTopBtn = document.getElementById("backToTopBtn");
//
//    // Show/hide button based on scroll position
//    window.addEventListener("scroll", function () {
//        if (window.scrollY > 200) {
//            backToTopBtn.style.display = "block"; // Show button
//        } else {
//            backToTopBtn.style.display = "none"; // Hide button
//        }
//    });
//
//    // Scroll smoothly to top when button is clicked
//    backToTopBtn.addEventListener("click", function () {
//        window.scrollTo({
//            top: 0,
//            behavior: "smooth"
//        });
//    });
//});


//patients

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
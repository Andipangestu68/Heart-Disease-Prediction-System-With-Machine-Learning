package com.heart.disease.predict.model;


import jakarta.persistence.*;

@Entity
@Table(name = "patient_data") // Nama tabel di database
public class PatientsData {

    // Primary key menggunakan SEQUENCE untuk PostgreSQL
    @Id
    @SequenceGenerator(
            name = "patient_data_seq",
            sequenceName = "patient_data_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_data_seq")
    private Long id;

    // Informasi pribadi pasien
    @Column(name = "name", nullable = false, length = 100)
    private String name; // Nama pasien

    @Column(name = "age", nullable = false)
    private int age; // Usia pasien

    @Column(name = "sex", nullable = false)
    private int sex; // Jenis kelamin (1 = Laki-laki, 0 = Perempuan)

    // Data medis pasien
    @Column(name = "cp", nullable = false)
    private int cp; // Tipe nyeri dada (Chest Pain)

    @Column(name = "trestbps", nullable = false)
    private int trestbps; // Tekanan darah saat istirahat (mm Hg)

    @Column(name = "chol", nullable = false)
    private int chol; // Kolesterol dalam darah (mg/dl)

    @Column(name = "fbs", nullable = false)
    private int fbs; // Gula darah puasa (>120 mg/dl: 1 = ya, 0 = tidak)

    @Column(name = "restecg", nullable = false)
    private int restecg; // Hasil elektrokardiografi istirahat

    @Column(name = "thalach", nullable = false)
    private int thalach; // Detak jantung maksimum yang tercatat (bpm)

    @Column(name = "exang", nullable = false)
    private int exang; // Angina yang diinduksi oleh olahraga (1 = ya, 0 = tidak)

    @Column(name = "oldpeak", nullable = false)
    private float oldpeak; // Depresi ST relatif terhadap istirahat

    @Column(name = "slope", nullable = false)
    private int slope; // Kemiringan segmen ST (Slope)

    @Column(name = "ca", nullable = false)
    private int ca; // Jumlah pembuluh darah utama yang diwarnai (0-3)

    @Column(name = "thal", nullable = false)
    private int thal; // Jenis thalassemia (3 = normal, 6 = cacat tetap, 7 = cacat reversibel)

    // Prediksi risiko penyakit jantung
    @Column(name = "risk", nullable = false, length = 50)
    private String risk; // Kategori risiko (misalnya "High", "Low")

    @Column(name = "at_risk_probability", nullable = false)
    private float atRiskProbability; // Probabilitas terkena risiko

    @Column(name = "no_risk_probability", nullable = false)
    private float noRiskProbability; // Probabilitas tidak terkena risiko

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getTrestbps() {
        return trestbps;
    }

    public void setTrestbps(int trestbps) {
        this.trestbps = trestbps;
    }

    public int getChol() {
        return chol;
    }

    public void setChol(int chol) {
        this.chol = chol;
    }

    public int getFbs() {
        return fbs;
    }

    public void setFbs(int fbs) {
        this.fbs = fbs;
    }

    public int getRestecg() {
        return restecg;
    }

    public void setRestecg(int restecg) {
        this.restecg = restecg;
    }

    public int getThalach() {
        return thalach;
    }

    public void setThalach(int thalach) {
        this.thalach = thalach;
    }

    public int getExang() {
        return exang;
    }

    public void setExang(int exang) {
        this.exang = exang;
    }

    public float getOldpeak() {
        return oldpeak;
    }

    public void setOldpeak(float oldpeak) {
        this.oldpeak = oldpeak;
    }

    public int getSlope() {
        return slope;
    }

    public void setSlope(int slope) {
        this.slope = slope;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    public int getThal() {
        return thal;
    }

    public void setThal(int thal) {
        this.thal = thal;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public float getAtRiskProbability() {
        return atRiskProbability;
    }

    public void setAtRiskProbability(float atRiskProbability) {
        this.atRiskProbability = atRiskProbability;
    }

    public float getNoRiskProbability() {
        return noRiskProbability;
    }

    public void setNoRiskProbability(float noRiskProbability) {
        this.noRiskProbability = noRiskProbability;
    }
}

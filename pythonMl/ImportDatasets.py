from ucimlrepo import fetch_ucirepo
import pandas as pd
from xgboost.testing import datasets

# Ambil dataset dengan ID 45 (Heart Disease dataset)
heart_disease = fetch_ucirepo(id=45)

# Dapatkan data fitur (X) dan target (y)
X = heart_disease.data.features
y = heart_disease.data.targets

# Gabungkan fitur dan target menjadi satu dataframe
heart_disease_df = pd.concat([X, y], axis=1)

# menampilkan datasets yang sudah di gabungkan antara colom fitur dan colom target
print(heart_disease_df)

# Simpan dataframe ke dalam file CSV
# heart_disease_df.to_csv('datasets/heart_disease_datasets.csv', index=False)

# Tampilkan metadata tentang dataset
print("Metadata:")
print(heart_disease.metadata)

# Tampilkan informasi variabel
print("\nInformasi Variabel:")
print(heart_disease.variables)
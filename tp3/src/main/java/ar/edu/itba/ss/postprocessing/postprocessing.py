import pandas as pd
from statistics_helper import dataframe_fp, dataframe_gas_law
from events_helper import animation_processing, evolution_fps

statistics_filepath = "fake_statistics.csv"
statistics = pd.read_csv(statistics_filepath, names=["presion","temperatura","volumen","n","tabique","tiempo"])

dataframe_fp(statistics) # Ejercicio 1.2 de la consigna
evolution_fps(
    [0.01,0.02],
    ["output/test_dynamic_1.xyz","output/test_dynamic_1.xyz"],
    "evolution_fps.png") # Ejercicio 1.2 de la consigna

dataframe_gas_law(statistics) # Ejercicio 1.3 de la consigna

# animation_processing("output/test_dynamic.xyz","post_processed_dynamic.xyz") # Para animaciones

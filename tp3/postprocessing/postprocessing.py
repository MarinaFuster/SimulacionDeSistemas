import pandas as pd
from statistics_helper import dataframe_fp, dataframe_gas_law
from events_helper import animation_processing, evolution_fps, dcm_calculator

statistics_filepath = "statistics.csv"
statistics = pd.read_csv(statistics_filepath, names=["presion","temperatura","volumen","n","tabique","tiempo"])

pressure_statistics_filepath = "PRESSURE_STATISTICS_N50.csv"
pressure_statistics = pd.read_csv(pressure_statistics_filepath, names=["presion","temperatura","volumen","n","tabique","tiempo"])

def evolution_of_fp():

    tabiques = statistics.tabique.unique()
    ns = statistics.n.unique()

    for n in ns:
        # dataframe_fp(statistics) # Ejercicio 1.2 de la consigna
        
        dynamic_paths = []
        for t in tabiques:
            dynamic_paths.append("output/test_dynamic_{}_{}.xyz".format(n,int(t*100)))
        
        evolution_fps(tabiques, dynamic_paths, "evolution_fps_{}.png".format(n)) # Ejercicio 1.2 de la consigna

def main():
    print(" \
    1. Evolution of fp\n \
    2. Gas Law PV=T\n \
    3. Calcula DCM de las partículas\n \
    4. Realizar animación de archivo determinado")

    result = int(input())
    if result == 1:
        evolution_of_fp()
    elif result == 2:
        dataframe_gas_law(pressure_statistics) # Ejercicio 1.3
    elif result == 3:
        print("Name of file to process (must be in output folder)")
        input_file = input()
        print("Name of output image (will be in results)")
        output_image = input()

        dcm_calculator("output/" + input_file, output_image) # Ejercicio 1.4
    elif result == 4:
        print("Name of file to process (must be in output folder)")
        input_file = input()

        print("Choose name of output file. This will be in results/")
        output_file = input()
        
        animation_processing("output/" + input_file, output_file)
    else:
        print("None of the options was selected")    

if __name__ == "__main__":
    main()
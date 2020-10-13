import matplotlib.pyplot as plt
import numpy as np
from math import log
from math import pow

from xyz_preprocessor import algorithms_information

print("Getting algorithms information...")

analytic, analytic_times, beeman, beeman_times, gearpc, gearpc_times, verlet, verlet_times = algorithms_information()

print("Finished algorithms information...")

beeman_errors = []
gearpc_errors = []
verlet_errors = []

print("Starting to plot different simulations...")

for i in range(analytic.size):
    
    plt.scatter(analytic_times[i], analytic[i], s=2, c='b')
    plt.xlabel("t [s]")
    plt.ylabel(f"Posición de la partícula [m]")
    plt.savefig(f"tp4/visualizations/analytic_oscillator_{8-i}.png")
    plt.clf()

    plt.scatter(beeman_times[i], beeman[i], s=2, c='c')
    plt.xlabel("t [s]")
    plt.ylabel(f"Posición de la partícula [m]")
    plt.savefig(f"tp4/visualizations/beeman_oscillator_{8-i}.png")
    plt.clf()

    plt.scatter(gearpc_times[i], gearpc[i], s=2, c='m')
    plt.xlabel("t [s]")
    plt.ylabel(f"Posición de la partícula [m]")
    plt.savefig(f"tp4/visualizations/gearpc_oscillator_{8-i}.png")
    plt.clf()

    plt.scatter(verlet_times[i], verlet[i], s=2, c='g')
    plt.xlabel("t [s]")
    plt.ylabel(f"Posición de la partícula [m]")
    plt.savefig(f"tp4/visualizations/verlet_oscillator_{8-i}.png")
    plt.clf()
    
    # assert(analytic[i].shape[0] == beeman[i].shape[0])
    # assert(analytic[i].shape[0] == gearpc[i].shape[0])
    # assert(analytic[i].shape[0] == verlet[i].shape[0])

    # beeman_err = np.sum((beeman[i] - analytic[i]) ** 2)/(analytic[i].shape[0])
    # gearpc_err = np.sum((gearpc[i] - analytic[i]) ** 2)/(analytic[i].shape[0])
    # verlet_err = np.sum((verlet[i] - analytic[i]) ** 2)/(analytic[i].shape[0])
    
    # beeman_errors.append(np.log(beeman_err))
    # gearpc_errors.append(np.log(gearpc_err))
    # verlet_errors.append(np.log(verlet_err))
    
    # beeman_errors.append(beeman_err)
    # gearpc_errors.append(gearpc_err)
    # verlet_errors.append(verlet_err)
    

# with open("errors_info.txt", "w") as f:
#     f.write("beeman\n")
#     for el in beeman_errors:
#         f.write(str(el))
#         f.write(",")
#     f.write("\ngearpc\n")
#     for el in gearpc_errors:
#         f.write(str(el))
#         f.write(",")
#     f.write("\nverlet\n")
#     for el in verlet_errors:
#         f.write(str(el))
#         f.write(",")

# beeman_errors = np.array(beeman_errors)
# gearpc_errors = np.array(gearpc_errors)
# verlet_errors = np.array(verlet_errors)

# print("Finished getting errors vs analytic...")

# steps = np.arange(start=-8,stop=-1,step=1)
# deltas = []
# for s in steps:
#     deltas.append("{:.0e}".format(pow(10, s)))
# deltas = np.array(deltas)

# ax = plt.gca()
# ax.set_yscale('log')

# beeman = plt.scatter(steps, beeman_errors)
# gearpc = plt.scatter(steps, gearpc_errors)
# verlet = plt.scatter(steps, verlet_errors)
# plt.xticks(steps, labels=deltas)
# plt.xlabel("delta t [s]")
# plt.ylabel(f"Error cuadrático medio [m\N{SUPERSCRIPT TWO}]")
# plt.legend((beeman, gearpc, verlet), ("Beeman", "Gearpc", "Verlet"))
# plt.savefig("tp4/results/errors.png")
# plt.clf()

print ("Finished program...")
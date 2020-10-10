import matplotlib.pyplot as plt
import numpy as np
from math import log

from xyz_preprocessor import algorithms_information

analytic, analytic_times, beeman, beeman_times, gearpc, gearpc_times, verlet, verlet_times = algorithms_information()

plt.scatter(analytic_times[0], analytic[0], s=2)
plt.savefig("tp4/results/analytic_oscillator.png")
plt.clf()

plt.scatter(beeman_times[0], beeman[0], s=2)
plt.savefig("tp4/results/beeman_oscillator.png")
plt.clf()

plt.scatter(gearpc_times[0], gearpc[0], s=2)
plt.savefig("tp4/results/gearpc_oscillator.png")
plt.clf()

plt.scatter(verlet_times[0], verlet[0], s=2)
plt.savefig("tp4/results/verlet_oscillator.png")
plt.clf()

beeman_errors = []
gearpc_errors = []
verlet_errors = []

for i in range(analytic.size):
    beeman_errors.append(np.square(beeman[i] - analytic[i]).mean())
    gearpc_errors.append(np.square(gearpc[i] - analytic[i]).mean())
    verlet_errors.append(np.square(verlet[i] - analytic[i]).mean())

beeman_errors = np.array(beeman_errors)
gearpc_errors = np.array(gearpc_errors)
verlet_errors = np.array(verlet_errors)

deltas = np.arange(start=0.0001, stop=0.0011, step=0.0001)

plt.scatter(deltas, beeman_errors)
plt.savefig("tp4/results/beeman_errors.png")
plt.clf()

plt.scatter(deltas, gearpc_errors)
plt.savefig("tp4/results/gearpc_errors.png")
plt.clf()

plt.scatter(deltas, verlet_errors)
plt.savefig("tp4/results/verlet_errors.png")
plt.clf()

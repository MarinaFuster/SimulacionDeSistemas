import matplotlib.pyplot as plt
import math
import numpy as np
from glob import glob

prefix = "orbit"
train_folder = "tp4/output"

m_earth = float(5.97219 * math.pow(10,24))
m_sun = float(1988500 * math.pow(10,24))
m_mars = float(6.4171 * math.pow(10,23))

G =  float(6.67430 * math.pow(10,-11))


def dist(x1, y1, x2, y2):
    return math.sqrt((x1-x2) ** 2 + (y1-y2) ** 2)

print("About to analyze...")

deltaT = [5, 50, 2, 10, 100, 1000, 10000, 100000, 1000000]

filenames = np.sort(glob(f"{train_folder}/{prefix}*"))[::-1]
print(filenames)
 
deltaE = []

first_energies = []
last_energies = []

for f in filenames:
    print(f"Analyzing {f}...")
        
    with open(f) as in_f:
        energies = []
        
        for line in in_f:
            print("Analyzing energy...")
            elements = line.split("\t")
            
            x_earth = float(elements[0])
            y_earth = float(elements[1])
            vx_earth = float(elements[2])
            vy_earth = float(elements[3])
            
            x_mars = float(elements[4])
            y_mars = float(elements[5])
            vx_mars = float(elements[6])
            vy_mars = float(elements[7])

            E_kinetic = 0.5 * (m_earth*(vx_earth ** 2 + vy_earth ** 2) + m_mars*(vx_mars ** 2 + vy_mars ** 2))
            E_pot = -1 * G *( m_earth * m_sun / dist(x_earth, y_earth, 0, 0) \
                + m_earth * m_mars / dist(x_earth, y_earth, x_mars, y_mars) \
                     + m_sun * m_mars / dist(0, 0, x_mars, y_mars) )

            energies.append(E_kinetic+E_pot)

        deltaE.append(math.fabs(energies[0]-energies[1]))
        first_energies.append(math.fabs(energies[0]))
        last_energies.append(energies[1])
    
print(deltaE[1]/math.fabs(first_energies[1]))
# print(last_energies)

# delta_plot = plt.scatter(deltaT, deltaE, c='m')
# plt.xlabel("delta t [s]")
# plt.ylabel("delta E [J]")
# ax = plt.gca()
# ax.set_yscale('log')
# ax.set_xscale('log')

# # plt.savefig("tp4/results/energy.png")
# plt.show()
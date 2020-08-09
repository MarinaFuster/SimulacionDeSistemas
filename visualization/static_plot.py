import matplotlib.pyplot as plt
import numpy as np

# Plots space of the system where the particles will be
# L represents the side of the area
# MxM is the amount of cells in the area. If M=0, no cells will be plotted
def plot_system_space(L, M=0):
    
    fig, ax = plt.subplots()
    
    # intervals = M if M >= 1 else 1
    # axis = np.linspace(0, L, intervals)

    plt.xlim(0, L)
    plt.ylim(0, L)

    # rectangle = plt.Rectangle((0,0), L, L, fill=False)
    # ax.add_patch(rectangle)

    # Create grid
    if M > 1:
        for i in range(0, M):
            horiz_x, horiz_y = [0, 20], [i*(L/M), i*(L/M)]
            vert_x, vert_y = [i*(L/M), i*(L/M)], [0, 20]
            plt.plot(horiz_x, horiz_y, vert_x, vert_y, color='#E6E6E6', linestyle='-')

    plt.suptitle("System's Space\nSpace area: {}, Cell quantity: {}".format(L*L, M*M))
    plt.show()
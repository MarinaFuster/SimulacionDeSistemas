import matplotlib.pyplot as plt
from system_plot import plot_space, plot_particles_in_space, plot_neighbours
from system_configuration import SystemConfiguration

def main():
    # system = SystemConfiguration('Static100.txt')
    # system.dynamic_configuration('Dynamic100.txt')
    # system.neighbours_configuration('AlgunosVecinos_100_rc6.txt')

    plot_particles_in_space(system)
    # plot_neighbours(system, system.particles[2])

if __name__ == '__main__':
    main()
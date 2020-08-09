from static_plot import plot_system_space
from system_configuration import SystemConfiguration

def main():
    # plot_system_space(20, M=1)
    system = SystemConfiguration('Static100.txt', 'Dynamic100.txt')
    print(system)

if __name__ == '__main__':
    main()
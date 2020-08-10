import matplotlib.pyplot as plt
import numpy as np
from sklearn.preprocessing import normalize

def scatter_plot_2D(X, Y, color='blue', title="Fill title", x_label="Fill X label", y_label="Fill Y label"):
    plt.scatter(X, Y)
    plt.title(title)
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    
    plt.show()

def scatter_plot_3D(X, Y, Z, color='blue', title="Time efficiency [ns]", x_label="M, cell qty", y_label="N, particles qty"):

    # Creating figure 
    fig = plt.figure(figsize = (10, 7)) 
    ax = plt.axes(projection ="3d")
    
    # Creating plot 
    ax.scatter3D(X, Y, Z, color=color); 
    plt.title(title) 
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    
    # show plot 
    plt.show()

if __name__ == '__main__':
    f = open('timeDifferentNandM.txt', 'r')
    lines = f.readlines()
    N = []
    M = []
    T = []
    for line in lines:
        values = line.split(',')
        N.append(int(values[0]))
        M.append(int(values[1]))
        T.append(int(values[2]))
    scatter_plot_3D(N, M, T) 
    f.close()       
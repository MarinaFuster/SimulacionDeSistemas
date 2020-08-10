import matplotlib.pyplot as plt

def scatter_plot(X, Y, color='blue', title="Fill title", x_label="Fill X label", y_label="Fill Y label"):
    plt.scatter(X, Y)
    plt.title(title)
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    
    plt.show()

if __name__ == '__main__':
    pass
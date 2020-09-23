import matplotlib.pyplot as plt

from enum import Enum

folder = "src/main/java/ar/edu/itba/ss/postprocessing/results/"

DELIMITER = ','

class DynamicInputState(Enum):
    START = 1
    COMMENT = 2
    PARTICLE = 3

def information_list(dynamic_input_path):
    times = []
    fps = []
    state = DynamicInputState.START
    remaining = 0
    with open(dynamic_input_path) as in_f:
        for line in in_f:
            if state == DynamicInputState.START:
                remaining = int(line)
                state = DynamicInputState.COMMENT
            elif state == DynamicInputState.COMMENT:
                if line != '\n':
                    comments = line.split(DELIMITER)
                    times.append(float(comments[0]))
                    fps.append(float(comments[1]))
                if remaining == 0:
                    state = DynamicInputState.START
                else:
                    state = DynamicInputState.PARTICLE
            elif state == DynamicInputState.PARTICLE:
                remaining -= 1
                if remaining == 0:
                    state = DynamicInputState.START
    return times, fps

def animation_processing(dynamic_input_path, dynamic_output_path):

    # List of events time and list of fps
    times, fps = information_list(dynamic_input_path)

    if(times.__len__() == 0):
        print("No times registered")
        exit()
    
    last_time = int(times[times.__len__()-1]) # we will make sure there's an bigger or equals time

    # evenly spaced times for animation
    evenly_spaced_times = [i for i in range(last_time)]

    output_path = folder + dynamic_output_path

    state = DynamicInputState.START
    remaining = 0
    current_time_index = 0 # I will go through all evenly spaced times

    with open(dynamic_input_path) as in_f:
        with open(output_path, "w") as out_f:

            # For now im not sure if I will write the first line
            current_n_line = None
            write_particles = False

            for line in in_f:
                # first line with number of particles
                if state == DynamicInputState.START:
                    remaining = int(line)
                    current_n_line = line
                    state = DynamicInputState.COMMENT
                # second line of comments will have time on it
                elif state == DynamicInputState.COMMENT:
                    comments = line.split(DELIMITER)
                    time = float(comments[0]) # current time
                    if time >= evenly_spaced_times[current_time_index]: # we will write this event
                        out_f.write(current_n_line)
                        out_f.write(line)
                        write_particles = True   
                        current_time_index = current_time_index + 1
                    if remaining == 0:
                        write_particles = False
                        state = DynamicInputState.START
                    else:
                        state = DynamicInputState.PARTICLE
                # lines of particles will be written depending on write_particles
                elif state == DynamicInputState.PARTICLE:
                    if write_particles:
                        out_f.write(line)
                    remaining -= 1
                    if remaining == 0:
                        state = DynamicInputState.START
                        write_particles = False

def evolution_fps(tabiques, dynamic_input_paths, output_file_name):

    if tabiques.__len__() != dynamic_input_paths.__len__():
        print("Wrong arguments in evolution fps")
        exit()

    plt.xlabel("Tiempo [s]")
    plt.ylabel("fp (fracción de partículas)")

    for i in range(tabiques.__len__()):
        times, fps = information_list(dynamic_input_paths[i])

        if(times.__len__() == 0):
            print("No times registered")
            exit()
    
        plt.plot(times, fps,label="Apertura {} [m]".format(tabiques[i]))
    
    plt.legend()
    filepath = folder + output_file_name
    plt.savefig(filepath, bbox_inches="tight", pad_inches=0.3)
    plt.clf()

#include <thread>
#include <mutex>

namespace ts {
    ::std::mutex cout_lock {  };

    // thread safe cout
    template <typename... T>
    void print(T&&... v) {
        ::std::lock_guard<::std::mutex> lk{ cout_lock };
        ::std::cout << ::std::forward<T>(v);
    }
}

void reciver_thread_func(std::mutex &cout_lock) {
    // if receive a change from server
    // print out the value

    {
        mlckgrd_t lk { cout_lock };
        std::cout << "Server changed: " << std::endl;
    }
}

void emulate_garage_door() {
    using std::cin;

    ts::print("Select garage door:")
    ts::print("0. Quit");
    ts::print("1. 1-Car");
    ts::print("2. 2-Car");

    ts::print(":: ");
    int choice = -1;
    cin >> choice;

    if (choice == 0) {
        return;
    }

    // get state
    int current_state = 1;

    ts::print("Select new state of ", choice, "-Car garage:")
    ts::print("0. Closed", ((current_state == 0)? " [Current]", ""));
    ts::print("1. Open", ((current_state == 1)? " [Current]", ""));

    ts::print(":: ");
    int state = -1;
    cin >> state;

    
}

int main(int argc, char **argv) {   
    using std::cin;

    bool quit = false;
    do {
        ts::print("Select one sensors from this list to become:");
        ts::print("0. Quit")
        ts::print("1. Garage Door");
        ts::print("2. Thermostat");
        ts::print("3. Light Dimmer")
        ts::print("4. Door Lock");
        ts::print("5. Door/Window Sensor");
        ts::print("6. Motion Sensor");

        ts::print(":: ")
        int choice = -1;
        cin >> choice;

        switch (choice) {
            case 0: quit = true; break;
            case 1: emulate_garage_door(); break;
            case 2: emulate_thermostat(); break;
            case 3: emulate_light_dimmer(); break;
            case 4: emulate_door_lock(); break;
            case 5: emulate_door_window_sensor(); break;
            case 6: emulate_motion_sensor(); break;
            default: ts::print("Incorrect choice, please try again. ") break;
        }
    } while (!quit);

    return 0;
}
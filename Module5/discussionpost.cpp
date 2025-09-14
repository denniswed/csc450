#include <iostream>
#include <sstream>
#include <stop_token> // Add this line for std::stop_token
#include <thread>

using namespace std::chrono_literals;

void demonstrate_jthread_features() {
  std::cout << "=== C++20 jthread Demonstration ===\n";

  // C++20 jthread with automatic joining
  {
    std::cout << "Creating jthread that auto-joins...\n";
    std::jthread auto_thread([]() {
      std::ostringstream oss;
      oss << "Thread " << std::this_thread::get_id()
          << " running for 2 seconds\n";
      std::cout << oss.str();
      std::this_thread::sleep_for(2s);
      std::cout << "Auto-joining thread completed\n";
    });
    // Automatically joins when going out of scope!
  }

  std::cout << "First thread completed automatically\n";

  // jthread with stop token (C++20 cooperative cancellation)
  {
    std::cout << "\nCreating cancellable jthread...\n";
    std::jthread cancellable_thread([](std::stop_token token) {
      for (int i = 0; i < 10; ++i) {
        if (token.stop_requested()) {
          std::cout << "Thread received stop request, exiting gracefully\n";
          return;
        }
        std::cout << "Working... " << i << "\n";
        std::this_thread::sleep_for(500ms);
      }
    });

    // Let it run for a bit, then request stop
    std::this_thread::sleep_for(2s);
    cancellable_thread.request_stop();
    // Automatically joins here too!
  }

  std::cout << "All threads completed!\n";
}

int main() {
  try {
    demonstrate_jthread_features();
  } catch (const std::exception &e) {
    std::cout << "Error: " << e.what() << "\n";
    return 1;
  }
  return 0;
}
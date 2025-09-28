#include <condition_variable>
#include <iostream>
#include <mutex>
#include <queue>
#include <thread>

std::mutex mtx;
std::condition_variable cv;
std::queue<int> dataQueue;
bool done = false;

void producer() {
  for (int i = 0; i < 5; ++i) {
    {
      std::lock_guard<std::mutex> lock(mtx);
      dataQueue.push(i);
    }
    cv.notify_one();  // wake processor
  }
  {
    std::lock_guard<std::mutex> lock(mtx);
    done = true;
  }
  cv.notify_one();
}

void processor() {
  while (true) {
    std::unique_lock<std::mutex> lock(mtx);
    cv.wait(lock, [] { return !dataQueue.empty() || done; });
    while (!dataQueue.empty()) {
      std::cout << "Processed: " << dataQueue.front() << "\n";
      dataQueue.pop();
    }
    if (done)
      break;
  }
}

int main() {
  std::thread t1(producer);
  std::thread t2(processor);
  t1.join();
  t2.join();
  return 0;
}
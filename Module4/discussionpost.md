One example of a vulnerabilt/issue with using iostreams is that changes to the stream state are globally persistent. For instance running this snippet, even in a separate function, output will be converted to hex and capital.

`std::cout << std::hex << std::uppercase; // Changes global state`

In the full code, you can see that when displaying the account info prior to running process_user_data, it displays as intended, the account number of 12345. But after running process_user_data it displays as HEX.To resolve (see below) you need to save the stream state prior to changing it and then restore it (there is a display of the account info prior to restoring stream state.)

Another interesting vulnerability is related to precision and allowing users to set such precision. In the code below (same program as above) the function demonstrate_precision_vulnerability declares some values and emulates letting the user set precision for the stream output. In the case below the output is set to 50 in length but the integer value set by the variable is much smaller. The output ends up providing unknown data. The second part of that function does something similar only it sets the std:setw (the length of the output) but again the actual value of the data is small. In this case it makes it looks like the output value ("test") is padded to the right by 100 characters.

You can see a safe way of managing it with the demonstrate_safe_precision function where the user input is collected for precision and width again (emulated) but then there is a MAX setting for both set as well. safe_precision and safe_width basically takes the MAX setting and compares to the user setting and selects the smallest one. At the end then stream state is always restored again.

The examples are not terribly realistic but the get the point across, especially around managing user input, or any input (couuld be from external functions or api calls) and making sure that you save the stream state information and always restore after such functions. Additionally, you always want to put guardrails around certain functionality that can lead to unintended consequences.

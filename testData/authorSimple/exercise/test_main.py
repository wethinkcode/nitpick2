# this is the test that would grade a submission
import sys
import unittest

sys.path.append(".")

from main import say_hello


class Testing(unittest.TestCase):
    def test_say_hello(self):
        self.assertEqual("hello", say_hello())


if __name__ == "__main__":
    unittest.main()

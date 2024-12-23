cd $EXERCISE_FOLDER
python test_main.py &>/dev/null
if [ $? == 0 ]; then
    echo ">>>ResultPass"
    echo "You passed."
    echo "<<<"
else
  echo ">>>ResultFail"
  echo "You failed."
  echo "<<<"
fi




**Test Cases**


## **Login/Create Account Functionality**


## **Test 1: User login with correct credentials**

Steps:



1. User launches application.
2. User selects the “Log in!” Button.
3. User selects the full name textbox. 
4. User enters the full name via the keyboard.
5. User selects the username textbox.
6. User enters the username via the keyboard.
7. User selects the password textbox.
8. User enters the password via the keyboard.
9. User selects the "Log in" button corresponding to their status (teacher or student).

Expected result: Application verifies the user's full name, username, password, and role then loads their main menu.

Test Status: Passed. 


### **Test 2: Create Account with all fields**

Steps:



1. User launches application.
2. User selects the “Create Account” Button.
3. User selects the full name textbox. 
4. User enters the full name via the keyboard.
5. User selects the username textbox.
6. User enters the username via the keyboard.
7. User selects the password textbox.
8. User enters the password via the keyboard.
9. User selects the "Create Account" button corresponding to their status (teacher or student).

Output: Take the user to landing 

Expected result: Application creates an account with the given information. The user is then directed to the main menu GUI.

Test Status: Passed. 


### **Test 3: User login with wrong case credentials **

Steps:



1. User launches application.
1. User selects the “Log in!” Button.
2. User selects the full name in the textbox. 
3. User enters the full name in the wrong case via the keyboard. (Ex: expected “Teacher”, received “teacher”)
4. User selects the username textbox.
5. User enters the username in the wrong case via the keyboard. (Ex: expected “Name”, received “name”)
6. User selects the password textbox.
7. User enters the password in the wrong case via the keyboard.
8. User selects the "Log in" button corresponding to their status (teacher or student).

Output: Log In Failed

Expected result: Log In Failed, the credentials are case sensitive

Test Status: Passed. 


### **Test 4: User login with incorrect credentials **

Steps:



1. User launches application.
1. User selects the “Log in!” Button.
2. User selects the full name in the textbox. 
3. User enters an unregistered full name via the keyboard.
4. User selects the username textbox.
5. User enters the unregistered username via the keyboard. 
6. User selects the password textbox.
7. User enters an unregistered password via the keyboard.
8. User selects the "Log in" button corresponding to their status (teacher or student).

Output: Log In Failed

Expected result: The credentials need to be registered to be recognized as a user. 

Test Status: Passed. 


### **Test 5: Create user with empty fields **

Steps:



1. User launches application.
1. User selects the “Log in!” Button.
2. User selects the full name in the textbox. 
3. User enters an empty field.
4. User selects the username textbox.
5. User enters an empty field. 
6. User selects the password textbox.
7. User enters an empty field.
8. User selects the "Log in" button corresponding to their status (teacher or student).

Output: User should be prompted to fill in all the fields without leaving any blank.  

Expected result: User should be prompted to fill in all the fields without leaving any blank.  

Test Status: Passed 


## **Landing Page/Main Menu Functionalities **


## **Test 6: Edit account in accordance with user status**

Steps:



1. After the user is logged in, the landing page is visible.
2. User selects the “Edit Account” Button.
3. User selects the full name textbox. 
4. User enters the full name via the keyboard.
5. User selects the username textbox.
6. User enters the username via the keyboard.
7. User selects the password textbox.
8. User enters the password via the keyboard.
9. User selects the "Edit Account" button corresponding to their status (teacher or student).

Output: The user can log in with the changed credentials. 

Expected result: The user can log in with the changed credentials. 

Test Status: Passed. 


## **Test 7: Delete account button and stop before deleting**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Delete Account” Button.
3. User is prompted with “Enter password to continue”
4. User enters the current password 
5. User is prompted with a warning window, “do you want to delete the account?” 
6. User selects Cancel

Output: User returns to Landing Page 

Expected result: User should not have deleted the account 

Test Status: Passed. 


## **Test 8: Delete account button and complete deletion **


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Delete Account” Button.
3. User is prompted with “Enter password to continue”
4. User enters the current password 
5. User is prompted with a warning window, “do you want to delete the account?” 
6. User selects OK

Output: Account deleted message and program shuts down 

Expected result: Account to be deleted and not be accessible through login with same credentials

Test Status: Passed. 


## **Test 9: Take Quiz for Student (Normal)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Take Quiz” Button.
3. “Select Course” window pops up
4. Student selects the course they want to take quiz for 
5. “Select Quiz” window pops up 
6. Student selects the quiz they want to take
7. Quiz window pops up and timer starts 
8. Student selects all responses and submits quiz
9. Quiz Submitted window pops up 
10. User selects OK

Output: Program returns to landing page 

Expected result: Program worked as expected and let the student take a standard quiz

Test Status: Passed. 


## **Test 10: Take Quiz for Student (Quit midway)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Take Quiz” Button.
3. “Select Course” window pops up
4. Student selects the course they want to take quiz for 
5. “Select Quiz” window pops up 
6. Student selects the quiz they want to take
7. User enters the current password 
8. Quiz window pops up and timer starts 
9. Student selects all responses
10. Student does not select submit and clicks on exit quiz 

Output: Program returns to landing page 

Expected result: Program worked as expected and saves the submission regardless. 

Test Status: Passed. 


## **Test 11: Create course (specific to Teacher role)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Create Course” Button.
3. Teacher enters the field for course name
4. “Course successfully created” message pops up 
5. User selects ok to close the dialog

Output: Program returns to landing page 

Expected result: Program worked as expected and let the teacher create a course with a course name

Test Status: Passed. 


## **Test 12: Create quiz in a course and add question(specific to Teacher role)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Modify Course” Button.
3. User selects the quiz they want to modify 
4. “Welcome to [course name]” window pops up. 
5. User clicks on the create quiz button.
6. User selects the type of question (Multiple choice, T/F, Fill in the blank)
7. Add the question description in the insert question text field
8. Specifies the correct answer
9. To add more questions, the user clicks on add question and repeats steps 6 through 8
10. After satisfied, the user clicks on “Save Quiz” button
11. The user is prompted to create a name for the quiz
12. Quiz saved dialog pops up
13. User selects ok to close the dialog

Output: Program returns to landing page 

Expected result: Program worked as expected and lets the teacher create a standard quiz

Test Status: Passed. 


## **Test 13: Create quiz in a course and delete question(specific to Teacher role)**


## **Steps:**



1. Assuming the user followed all steps to reach to this point
2. User clicks on the create quiz button.
3. User selects the type of question (Multiple choice, T/F, Fill in the blank)
4. Add the question description in the insert question text field
5. Specifies the correct answer
6. To add more questions, the user clicks on add question and repeats steps 6 through 8
7. User clicks on “Delete Question” button to get rid of unwanted questions

Output: The question gets deleted  

Expected result: Program worked as expected and lets the teacher delete a question

Test Status: Passed. 


## **Test 14: Create quiz in a course by importing file(specific to Teacher role)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Modify Course” Button.
3. User selects the quiz they want to modify 
4. “Welcome to [course name]” window pops up. 
5. User clicks on the create quiz button.
6. In the Quiz Creator window, the user clicks on import quiz
7. User selects the file which contains the quiz in a required format
8. After satisfied, the user clicks on “Save Quiz” button
9. The user is prompted to create a name for the quiz
10. Quiz saved dialog pops up
11. User selects ok to close the dialog

Output: Program returns to landing page 

Expected result: Program worked as expected and lets the teacher create a standard quiz

Test Status: Passed.


## **Test 15: Create quiz in a course clicking wrong buttons[pt. 1](specific to Teacher role)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Modify Course” Button.
3. User selects the quiz they want to modify 
4. “Welcome to [course name]” window pops up. 
5. User clicks on the create quiz button.
6. User selects the type of question (Multiple choice, T/F, Fill in the blank)
7. Add the question description in the insert question text field
8. Specifies the correct answer
9. To add more questions, the user clicks next button

Output: Program does nothing 

Expected result: Program worked as expected and does not add questions unless specified by the user 

Test Status: Passed.


## **Test 16: Create quiz in a course clicking wrong buttons[pt. 2](specific to Teacher role)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Modify Course” Button.
3. User selects the quiz they want to modify 
4. “Welcome to [course name]” window pops up. 
5. User clicks on the create quiz button.
6. User selects the type of question (Multiple choice, T/F, Fill in the blank)
7. Add the question description in the insert question text field
8. Specifies the correct answer
9. With only one question, the user attempts to click on delete question

Output: Program does nothing 

Expected result: Program worked as expected and let the user delete the first question, since at least 1 question should be asked in a quiz.

Test Status: Passed.


## **Test 17: Exit create quiz window without saving the quiz(specific to Teacher role)**


## **Steps:**



1. After the user is logged in, the landing page is visible.
2. User selects the “Modify Course” Button.
3. User selects the quiz they want to modify 
4. “Welcome to [course name]” window pops up. 
5. User clicks on the create quiz button.
6. User selects the type of question (Multiple choice, T/F, Fill in the blank)
7. Add the question description in the insert question text field
8. Specifies the correct answer
9. User clicks on exit button

Output: Program returns to modify quiz window 

Expected result: Program worked as expected and if the user exits without saving their progress, the quiz will be deleted.

Test Status: Passed.


## **Test 18: Edit quiz(specific to Teacher role)**


## **Steps:**



1. Assuming the has followed all the steps correctly to reach the modify quiz window
2. Under the “Modify quiz” user clicks on Edit quiz
3. User is prompted to select the quiz to edit
4. User is able to see the quiz creator window again
5. User makes changes to the quiz as desired

Output: Changes made in the specific quiz from the course are visible on backend and student end

Expected result: Program worked as expected and teacher is able to edit quiz without issues 

Test Status: Passed.


## **Test 19: Delete quiz(specific to Teacher role)**


## **Steps:**



1. Assuming the has followed all the steps correctly to reach the modify quiz window
2. Under the “Modify quiz” user clicks on Delete quiz
3. User is prompted to select the quiz to delete.
4. User selects the quiz
5. The specific quiz gets deleted and is not visible again

Output: Deleted quiz is not found either on backend or student end

Expected result: Program worked as expected and teacher is able to delete quiz without issues 

Test Status: Passed.


## **Test 20: View/Grade quiz(specific to Teacher role)**


## **Steps:**



1. Assuming the has followed all the steps correctly to reach the modify quiz window
2. Under the “Modify quiz” user clicks on Edit quiz
3. User is prompted to select the quiz to edit
4. User is able to see the quiz creator window again
5. User makes changes to the quiz as desired
6. 

Output: User is redirected to modify quiz window.

Expected result: Program worked as expected and teacher is able to view student submissions without issues 

Test Status: Passed.


## **Test 21: Modify grade for specific student(specific to Teacher role)**


## **Steps**



1. Assuming the has followed all the steps correctly to reach the submission window for specific student 
2. User clicks on edit grades 
3. User is prompted to select question number to modify grade for
4. User selects the desired question number
5. User is prompted with maximum points earned
6. User enters the desired score and clicks on Ok button to save or cancel
7. User gets prompted for maximum points earned for the specific question 
8. User enters the desired score and clicks on Ok button to save or cancel
9. User clicks on close button

Output: User is redirected to modify quiz window.

Expected result: Program worked as expected and teacher is able to view student submissions and edit scores without issues.

Test Status: Passed.


## **Test 22: Modify quiz for teacher and student takes edited version (Concurrency Test)**


## **Steps**



1. **Teacher does the following:**
2. Assuming the has followed all the steps correctly to reach the modify quiz window
3. Under the “Modify quiz” user clicks on Edit quiz
4. User is prompted to select the quiz to edit
5. User is able to see the quiz creator window again
6. User makes changes to the quiz as desired
7. **Now the student does the following:**
8. After the user is logged in, the landing page is visible.
9. User selects the “Take Quiz” Button.
10. “Select Course” window pops up
11. Student selects the course they want to take quiz for 
12. “Select Quiz” window pops up 
13. Student selects the quiz they want to take
14. Quiz window pops up and timer starts 
15. Student selects all responses and submits quiz
16. Quiz Submitted window pops up 
17. User selects OK

Output: The student takes the edited quiz version

Expected result: The student takes the edited quiz version rather than the old version.

Test Status: Passed.
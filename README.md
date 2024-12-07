# Doctor Appointment App
From Vu Xuan Hoang, Nguyen Hoang Hao, Bui Ngoc Hiep UTEk21

## Introduction
Doctor Appointment App is an application that allows users to make appointments with doctors, manage appointments, and look up related information.
This app provides many features to help doctors and patients manage their appointments and other features make the system more diverse.
There are 5 roles can interact with the system:
- Guest
- Patient
- Doctor
- Nurse
- Admin

## Guest
Guests can make their appointment without login to the system, when booking success a mail contains appointment details with send to your inputted email.
Guests can login to the system to access more features.

## Patient
Patients have more features to interact than guest, they can book an appointment, reschedule, cancel, and give an review to the doctor.\
Tracking medical records through many treatments can help patients know more about their treatment history.
Chat realtime with doctor also provided.

## Doctor
Doctors can manage their appointment through appointments dashboard or calendar.

## Nurse
Nurses can create and edit a medical record from an appointment but only when it in progress status.

## Admin
Admin have the highest privilege in the system with can manage every data, such as: appointments, doctors, patients,...

## Chatbot
We use Ollama3.2 as an assistant in answer questions about sports, medical, medicine, foods, healthcare...

## If you clone this repo to your machine, some walkthroughs you can follow:
- Create a database name 'doc_appointment_app', or what ever name you want.
- npm install to install all dependencies from frontend and frontend-admin
- Install Ollama3.2 (optional)

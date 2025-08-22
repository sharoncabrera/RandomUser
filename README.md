# 📱 Random User Challenge

This project is my solution for the **Random User** coding challenge.  

---

## 🏗️ Architecture & Design
- Modular **MVVM + Clean Architecture** to promote separation of concerns, testability, and maintainability.  
- Project modules:  
  - `app` → UI  
  - `data` → data sources & repository  
  - `domain` → use cases & business models  
- Tech stack:  
  - **Jetpack Compose** → UI  
  - **Hilt** → dependency injection  
  - **Room** → local persistence  
  - **Retrofit** → network requests  
  - **Coil** → image loading
   - **Moshi** → JSON parsing (chosen for its good integration with Retrofit)  

---

## ✨ Features Implemented
- **Fetch & display users** from [randomuser.me](https://randomuser.me).  
- **Duplicate handling**: users are uniquely identified by the API’s `login.uuid`.  
- **User list view** showing name, email, picture, and phone.  
- **Infinite scroll**: loads more users as you scroll.  
- **Delete users**: persistently mark users as deleted so they don’t show up again.  
- **Filtering**: live search by first name, last name, or email.  
- **User detail screen**: includes gender, full name, location (street, city, state), registration date, email, and picture.  
- **Persistence**: all user data (including deletion state) stored locally with **Room**, surviving app restarts.  

---

## ✅ Testing
- Unit tests for:  
  - **ViewModels**  
    - **Duplicate handling**  
    - **Filtering logic**  
    - **Deleting a user** (verifying that once deleted, they are no longer retrieved)  
    - **Fetching a list of users**  
  - **Use Cases**  

---

## 📌 Assumptions & Notes
- **Unique identifier**: `login.uuid` is used as the unique key for users.  
- **Error handling**: basic handling for network issues and Local database issues (e.g., showing a message).
- **Loading states**: the UI shows loading indicators while fetching data.  
- **API optimization**: requests specify `?results=X` for efficient loading. 

---

## 🚀 Improvements
- Stronger error handling (e.g., retries).  
- Add more **testing**.  
- Expanded with retries or a better error UI.
- Extract hardcoded **strings** from the code into `strings.xml`


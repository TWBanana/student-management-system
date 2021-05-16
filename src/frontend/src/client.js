import fetch from 'unfetch';

const checkStatus = response => {
    if (response.ok) {
        return response;
    }
    // convert non-2XX HTTP responses into errors
    const error = new Error(response.statusText);
    error.response = response;
    return Promise.reject(error);
}


export const getAllStudents = () =>
    fetch("students")
        .then(checkStatus);

export const addNewStudent = student =>
    fetch("students", {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(student)
    }).then(checkStatus)

export const deleteStudent = studentId =>
    fetch(`students/${studentId}`, {
        method: 'DELETE'
    }).then(checkStatus);
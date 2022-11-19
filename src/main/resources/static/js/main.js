// Print Users table
$(async function () {
    console.log("async printUser()")
    await printUsers();
});
const table = $('#bodyUsersTable');

async function printUsers() {
    console.log("PrintUser()")
    fetch("http://localhost:8080/api/v1/admin/users")
        .then(response => response.json())
        .then(users => {
            users.forEach(user => {
                let roleStr = "";
                user.roles.forEach(role => {
                    roleStr += role['noPrefixName'] + " ";
                })
                let tableUsers = `$(
                    <tr>
                       <td>${user.id}</td>
                       <td>${user.firstName}</td>
                       <td>${user.lastName}</td>
                       <td>${user.age}</td>
                       <td>${user.login}</td>
                       <td>${roleStr}</td>

                       <td>
                           <button type="button" class="btn btn-info" data-bs-toggle="modal" id="buttonEdit"
                            data-action="editModal" data-id="${user.id}" data-bs-target="#editModal">Edit</button>
                       </td>
                       <td>
                           <button type="button" class="btn btn-danger" data-bs-toggle="modal" id="buttonDelete"
                           data-action="delete" data-id="${user.id}" data-bs-target="#deleteModal">Delete</button>
                       </td>
                    </tr>
            )`;
                table.append(tableUsers);
            })
        })
}

// Get User
async function getUser(id) {
    console.log("getUser()")
    let response = await fetch("http://localhost:8080/api/v1/user/" + id);
    return response.json();
}

// Add User
$(async function() {
    await addUser();
});

function addUser() {
    fetch("http://localhost:8080/api/v1/admin/roles")
        .then(response => response.json())
        .then(roles => {
            let options, select = document.getElementById('rolesNewUser');
            roles.forEach(function (role) {
                options += `<option value="${role.value}">${role['noPrefixName']}</option>`;
            });
            select.innerHTML = options;
        });


    let formNewUser = document.forms["newUserForm"];
    let selectedRoleValues = [];
    formNewUser.addEventListener("submit", event => {
        console.log("clicking submit New User")
        event.preventDefault();

        if (formNewUser.roleEditList !== undefined) {
            for (let i = 0; i < formNewUser.roleEditList.length; i++) {
                if (formNewUser.roleEditList.options[i].selected) {
                    selectedRoleValues.push("ROLE_" + formNewUser.roleEditList.options[i].text)
                }
                console.log(selectedRoleValues)
            }
        }

        console.log("New User pre fetch()")
        console.log("FROM New User FORM DATA !!!!!!!!!!!")
        console.log(formNewUser.id.value)
        console.log(formNewUser.firstName.value)
        console.log(formNewUser.lastName.value)
        console.log(formNewUser.age.value)
        console.log(formNewUser.login.value)
        console.log("Roles Array: " + selectedRoleValues)

        fetch("http://localhost:8080/api/v1/admin/new", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formNewUser.id.value,
                firstName: formNewUser.firstName.value,
                lastName: formNewUser.lastName.value,
                age: formNewUser.age.value,
                login: formNewUser.login.value,
                password: formNewUser.password.value,
                roles: selectedRoleValues
            })
        })
            .then(() =>{
                formNewUser.reset();
                refreshUsersTable();
            })

    })

}

// Edit User
$('#editModal').on('show.bs.modal', function (event) {
    console.log("Show Edit Modal")
    let button = $(event.relatedTarget);
    let id = button.data('id');
    showEditModal(id)
        .catch(err => console.log(err));
})

async function showEditModal(id) {
    console.log("showEditModal()")
    let user = await getUser(id);
    let formEdit = document.forms["editUserForm"];
    formEdit.id.value = user.id
    formEdit.firstName.value = user.firstName
    formEdit.lastName.value = user.lastName
    formEdit.age.value = user.age
    formEdit.login.value = user.login
    formEdit.password.value = user.password

    fetch("http://localhost:8080/api/v1/admin/roles")
        .then(response => response.json())
        .then(roles => {
            let options, select = document.getElementById('roleEdit');
            roles.forEach(function (role) {
                options += `<option value="${role.value}">${role['noPrefixName']}</option>`;
            });
            select.innerHTML = options;
        });
}

$(async function () {
    console.log("async editUser()")
    editUser();
});

function editUser() {
    console.log("editUser()")
    let formEdit = document.forms["editUserForm"];
    let selectedRoleValues = []
    formEdit.addEventListener("submit", event => {
        console.log("clicking submit Edit User")
        event.preventDefault();

        if (formEdit.roleEditList !== undefined) {
            for (let i = 0; i < formEdit.roleEditList.length; i++) {
                if (formEdit.roleEditList.options[i].selected) {
                    selectedRoleValues.push("ROLE_" + formEdit.roleEditList.options[i].text)
                }
                console.log(selectedRoleValues)
            }
        }

        console.log("pre fetch()")
        console.log("FROM FORM DATA !!!!!!!!!!!")
        console.log(formEdit.id.value)
        console.log(formEdit.firstName.value)
        console.log(formEdit.lastName.value)
        console.log(formEdit.age.value)
        console.log(formEdit.login.value)
        console.log("Roles Array: " + selectedRoleValues)

        fetch("http://localhost:8080/api/v1/admin/update/" + formEdit.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formEdit.id.value,
                firstName: formEdit.firstName.value,
                lastName: formEdit.lastName.value,
                age: formEdit.age.value,
                login: formEdit.login.value,
                password: formEdit.password.value,
                roles: selectedRoleValues
            })
        })
            .then(() => {
                $('#closeEditModal').click();
                refreshUsersTable();
                selectedRoleValues = [];
            })
        console.log("post fetch()")
    })
    console.log("end editUser()")
}

// Delete User
$('#deleteModal').on('show.bs.modal', function (event) {
    console.log("Show Delete Modal")
    let button = $(event.relatedTarget);
    let id = button.data('id');
    showDeleteModal(id);
})

async function showDeleteModal(id) {
    console.log("showDeleteModal()")
    let user = await getUser(id);
    let formDelete = document.forms["deleteUserForm"];
    formDelete.id.value = user.id
    formDelete.firstName.value = user.firstName
    formDelete.lastName.value = user.lastName
    formDelete.age.value = user.age
    formDelete.login.value = user.login

    fetch("http://localhost:8080/api/v1/admin/roles")
        .then(response => response.json())
        .then(roles => {
            let options, select = document.getElementById('roleDelete');
            roles.forEach(function (role) {
                options += `<option value="${role.value}">${role['noPrefixName']}</option>`;
            });
            select.innerHTML = options;
        });
}

$(async function () {
    console.log("async deleteUser()")
    deleteUser();
});

function refreshUsersTable() {
    let table = document.getElementById("bodyUsersTable");
    table.innerHTML = "";
    printUsers();
}

function deleteUser() {
    console.log("deleteUser()")

    let form = document.forms["deleteUserForm"];
    form.addEventListener("submit", event => {
        event.preventDefault();
        fetch("http://localhost:8080/api/v1/admin/delete/" + form.id.value, {
            method: 'DELETE',
        })
            .then(() => {
                $('#closeDeleteButton').click();
                refreshUsersTable();
            })
    })

}

// Print User Information
$(async function () {
    await printUserInfo();
})

async function printUserInfo() {
    fetch("http://localhost:8080/api/v1/user/")
        .then(response => response.json())
        .then(user => {
            $('#authUsername').append(user.username);

            let roleStr = "";
            user.roles.forEach(role => {
                roleStr += role['noPrefixName'] + " ";
            })

            $('#authUserRoles').append(roleStr);

            let userInfo = `$(<tr>
                        <td>${user.id}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.login}</td>
                        <td>${roleStr}</td>
                      </tr>)`;
            $('#userInfoTable').append(userInfo);
        })
}
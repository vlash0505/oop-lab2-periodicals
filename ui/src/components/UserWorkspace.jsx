import { Component } from "react";
import Greeting from "./Greeting.jsx";
import $ from "jquery"
import axios from "axios";

const UserWorkspace = props => {
    const listPeriodicals = () => {
        alert(props.token);
        axios.get('http://localhost:8180/user/periodicals', {
            headers: {
                'Authorization': 'bearer ' + props.token,
                'Content-Type': 'application/json'
            }
        })
            .then( response => {
                alert("hey");
                let data = response.data;
                alert(data);
                let select = $('#periodicals');
                select.empty();
                $.each(data, function (index, item) {
                    select.append('<option name="periodical" value="' + item.id + '">' + item.name + '</option>');
                });
            })
            .catch(error => alert(error));
        alert("wow");
    }

    const listReqs = () => {
        axios.get('http://localhost:8000/user/list_requests',
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            })
            .then((response) => {
                let data = response.data;
                let select = $('#reqs');
                select.empty();
                $.each(data, function (index, item) {
                    select.append('<option name="req" value="' + item.id_book + '">' + item.id_book + ' ' + item.accepted + '</option>');
                });
            });
    }

    const takeBook = () => {
        let select = $('#periodicals').find(":selected");
        axios.post('http://localhost:8000/user/take_book',
            {
                id_book: select.val()
            },
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            })
            .then((response) => {
                let data = response.data;
                let stat = $('#status');
                if (data.status == false) {
                    stat.text(data.message);
                } else {
                    localStorage.setItem("current_book", select.val());
                    $('#reading_book').text(localStorage.getItem("current_book"));
                    stat.text("Success");
                }

            });
    }

    const returnBook = () => {
        axios.post('http://localhost:8000/user/return_book',
            {
                id_book: localStorage.getItem("current_book")

            },
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            });
        localStorage.setItem("current_book", -1);
        $('#reading_book').text("-1");
    }

    const requestBook = () => {
        let select = $('#periodicals').find(":selected");
        axios.post('http://localhost:8080/user/request_book',
            {
                id_book: select.val()
            },
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            }
        );
    }

    const unrequestBook = () => {
        let select = $('#reqs').find(":selected");
        axios.post('http://localhost:8080/user/unrequest_book',
            {
                id_book: select.val()
            },
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            });
    }

    const takeHome = () => {
        let select = $('#reqs').find(":selected");
        axios.post('http://localhost:8080/user/take_home',
            {
                id_book: select.val()
            },
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            })
            .then((response) => {
                let ans = response.data;
                if (ans.status == false) {
                } else {
                    localStorage.setItem("current_book", select.val());
                    $('#reading_book').text(localStorage.getItem("current_book"));
                }
            });
    }

    const findBook = () => {
        let idbook = $("#input_find").val();
        axios.get('http://localhost:8080/user/find_book',
            {
                id_book: idbook
            },
            {
                headers: {
                    'Authorization': 'bearer ' + props.token,
                    'Content-Type': 'application/json'
                }
            })
            .then((response) => {
                let data = response.data;
                let select = $("#found");
                select.empty();
                $.each(data, function (index, item) {
                    select.append('<option name="periodical" value="' + item.id + '">' + item.name + " " + item.amount + '</option>');
                });
            });
    }

    const changeReadingBook = () => {
        $('#reading_book').text(localStorage.getItem("current_book"));
    }

    return (
        <div width="100%">
            {/* <form autoComplete={"off"}> */}
                <Greeting />
                <br />
                Client
                <table width="100%" class="grid">
                    <tbody>
                        <tr>
                            <td>
                                <button id="list_books" onClick={listPeriodicals}>List periodicals</button>
                            </td>
                            <td>
                                <button id="list_reqs" onClick={listReqs}>List requests</button>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <select id="periodicals" size="3" width="100%"></select>
                            </td>
                            <td>
                                <select id="reqs" size="3"></select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <button id="take_book" onClick={takeBook}>Read periodical here</button>
                                <button id="request_book" onClick={requestBook}>Request periodical to home</button>
                                <button id="return periodical" onClick={returnBook}>Return periodical</button>
                            </td>
                            <td>
                                <button id="unrequest_book" onClick={unrequestBook}>Unrequest periodical</button>
                                <button id="take_home" onClick={takeHome}>Take periodical to home</button>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p id="status"> wooow </p>
                            </td>
                            <td>
                                You're reading a periodical with id =
                                <p id="reading_book"></p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input id="input_find"></input>
                                <button id="find" onClick={findBook}>Find</button>
                            </td>
                            <td>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <select id="found" size="5"></select>
                            </td>
                        </tr>
                    </tbody>
                </table>
            {/* </form> */}
        </div>
    )
}

export default ClientWorkspace;
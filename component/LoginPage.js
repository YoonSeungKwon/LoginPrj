import React, {useState} from "react";
import './style/form.css'
import axios from "axios";
import {useNavigate} from "react-router-dom";
const LoginPage = () =>{

    const navigate = new useNavigate();

    const [data, setData] = useState({
        email:"",
        password:"",
    })

    const handleChange = (e) =>{
        const{name, value} = e.target
        setData(prevState => ({
            ...prevState,
            [name]:value
        }))
    }

    const handleClick = (e) =>{
        axios.post("http://localhost:8080/api/v1/members/login", data
        ).then((response)=>{
            console.log(response)
            localStorage.setItem("ACCESS_TOKEN", response.headers.get("Authorization"))
            localStorage.setItem("REFRESH_TOKEN", response.headers.get("X-Refresh-Token"))
            navigate("/")
        }).catch((error)=>{
            console.log(error)
            alert(error.response.data.message + "   [" + error.response.data.code + "]")
        })
    }

    return(
        <>
            <h2>로그인</h2>
            <div className="form-form">
                <div className="form-line">
                    <span className="form-label">이메일: </span>
                    <input
                        className="form-input"
                        type="text"
                        name="email"
                        onChange={handleChange}
                        value={data.email}
                    />
                </div>
                <div className="form-line">
                    <span className="form-label">비밀번호: </span>
                    <input
                        className="form-input"
                        type="password"
                        name="password"
                        onChange={handleChange}
                        value={data.password}
                    />
                </div>
                <input
                    className="form-btn"
                    type="submit"
                    onClick={handleClick}
                    value="가입"
                />
            </div>
        </>
    );

}
export default LoginPage;
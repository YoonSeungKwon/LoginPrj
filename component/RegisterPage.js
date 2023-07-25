import React, {useState} from "react";
import axios from "axios";
import './style/form.css'
import {useNavigate} from "react-router-dom";
const RegisterPage = () =>{

    const navigate = new useNavigate();

    const [check, setCheck] = useState(false);

    const [data, setData] = useState({
        email:"",
        password:"",
        name:"",
    });

    const handleDuplication = (e) =>{
        axios.get(`http://localhost:8080/api/v1/members/check/${data.email}`
        ).then((response)=>{
            console.log(response)
            if(response.data){
                alert("이미 존재하는 이메일입니다.")
                setCheck(false)
            }
            else{
                alert("사용 가능한 이메일입니다")
                setCheck(true)
            }
        }).catch((error)=>{
            alert("이메일을 입력해 주세요.")
            console.log(error)
        })
    }

    const handleChange = (e) =>{
        const{name, value} =e.target
        setData(prevState => ({
            ...prevState,
            [name]:value
        }));
        if(e.target.name === "email")
            setCheck(false)
    };

    const handleClick = (e) =>{
        if(check) {
            axios.post("http://localhost:8080/api/v1/members/join", data
            ).then((response) => {
                console.log(response)
                alert(response.data.name + "님 환영합니다. 로그인 페이지로 이동합니다.")
                navigate("/api/v1/members/login")
            }).catch((error) => {
                console.log(error)
                alert(error.response.data.message + "   [" + error.response.data.code + "]")
            })
        }
        else{
            alert("이메일 중복체크를 해주세요.")
        }
    };

    return(
        <>
            <h2>회원가입</h2>
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
                    <input
                        type="button"
                        onClick={handleDuplication}
                        value="중복체크"
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
                <div className="form-line">
                    <span className="form-label">이름: </span>
                    <input
                        className="form-input"
                        type="text"
                        name="name"
                        onChange={handleChange}
                        value={data.name}
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
export default RegisterPage;
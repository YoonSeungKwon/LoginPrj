import React, {useEffect} from "react";
import {Link, useNavigate} from "react-router-dom";
import './style/all.css'
import axios from "axios";
const Home = () =>{

    const navigate = new useNavigate();

    const acc_headers = {
    headers:{
        Authorization:"Bearer " + localStorage.getItem("ACCESS_TOKEN"),
    }
    }

    const ref_headers = {
    headers:{
        Authorization:"Bearer " + localStorage.getItem("ACCESS_TOKEN"),
        'X-Refresh-Token':"Bearer " + localStorage.getItem("REFRESH_TOKEN")
    }
    }

    useEffect(()=>{
        axios.get("http://localhost:8080/api/v1/auth/infos", acc_headers)
            .then((response)=>{
                console.log(response)
            }).catch((error)=>{
                console.log(error)
                if(error.response.data.code === "TOKEN01"){
                    axios.get("http://localhost:8080/api/v1/auth/infos", ref_headers
                        ).then((response)=>{
                            console.log(response)
                            localStorage.setItem("ACCESS_TOKEN", response.headers.get("Authorization"))
                        }).catch((error)=>{
                            console.log(error)
                            if(error.response.data.code === "TOKEN02") {
                                localStorage.clear()
                                alert("로그인 기간이 만료되었습니다. 다시 로그인 해주세요.")
                                navigate("/api/v1/members/login")
                            }
                    })
                }
             })
    })

    const handleClickLogout = (e) =>{
        localStorage.clear()
        alert("로그아웃 되었습니다.")
        navigate("/")
    }

    return(
        <>
            <h1>Home Page</h1>
            <br/>
            <Link to="/api/v1/members/login">로그인 페이지로</Link>
            <br/>
            <Link to="/api/v1/members/join">회원가입 페이지로</Link>
            <br/>
            <Link to="/api/v1/members/info">사용자 정보</Link>
            <br/>
            <input
                type="button"
                onClick={handleClickLogout}
                value="로그아웃"
            />
        </>
    );

}
export default Home;
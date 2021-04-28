class LoginPart extends React.Component {
    constructor(props) {
        super(props);
        this.state = {sid : ''};
        this.sendLogin = this.sendLogin.bind(this);
        this.handleSid = this.handleSid.bind(this);
        this.handleSignup = this.handleSignup.bind(this);
    }

    sendLogin(event) {
        event.preventDefault();
        var params = {
            "studentId": this.state.sid
        };
        var queryString = Object.keys(params).map(function(key) {
            return key + '=' + params[key]
        }).join('&');

        fetch('login?' + queryString)
            .then(response => response.json())
            .then((data) => {
                if (data == true) ReactDOM.render(<HomePage />, document.getElementById('app'));
                else alert('شماره دانشجویی نامعتبر است!');
            });
    }

    handleSid(event) {
        this.setState(prevState => ({sid: event.target.value}));
    }

    handleSignup(event) {
        ReactDOM.render(<SignupPage />, document.getElementById('app'));
    }

    render() {
        return (
            <div className="container login_text">
                <h1>صفحه‌ی ورود</h1>

                <form onSubmit={this.sendLogin} className="login_form">
                    <div className="login_img_container">
                        <img src="../images/icons/login_avatar.png" alt="Avatar" className="login_avatar"/>
                    </div>

                    <div className="container login_container">
                        <label for="email"><b>ایمیل</b></label>
                        <input type="text" placeholder="ایمیل خود را وارد کنید." className="login_input" onChange={this.handleSid} required/>

                        <br/>

                        <label for="password"><b>رمز عبور</b></label>
                        <input type="password" placeholder="رمز خود را وارد کنید." className="login_input"/>
                        <button type="submit" className="login_button">ورود</button>

                        <button type="button" onClick={this.handleSignup} className="login_button">صفحه ثبت نام</button>
                    </div>
                </form>
            </div>
        );
    }
}

class LoginPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let x = false;
        fetch('loggedin_student')
            .then(resp => resp.json())
            .then(data => {
                x = !!Object.keys(data).length;
                if (x)
                    return ReactDOM.render(<HomePage />, document.getElementById('app'));
            });

        return (
            <div>
                <LoginPart />
                <BolbolestanFooter />
            </div>
        );
    }
}
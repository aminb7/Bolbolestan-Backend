class SignupPart extends React.Component {
    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
    }

    handleLogin(event) {
        ReactDOM.render(<LoginPage />, document.getElementById('app'));
    }

    render() {
        return (
            <div className="container login_text">
                <h1>صفحه‌ی ثبت نام</h1>

                <form className="login_form">
                    <div className="login_img_container">
                        <img src="../images/icons/login_avatar.png" alt="Avatar" className="login_avatar"/>
                    </div>

                    <div className="container login_container">
                        <label htmlFor="email"><b>نام</b></label>
                        <input type="text" placeholder="نام خود را وارد کنید." className="login_input"
                               onChange={this.handleSid} required/>

                        <br/>

                        <label htmlFor="email"><b>نام خانوادگی</b></label>
                        <input type="text" placeholder="نام خانوادگی خود را وارد کنید." className="login_input"
                               onChange={this.handleSid} required/>

                        <br/>

                        <label for="email"><b>ایمیل</b></label>
                        <input type="text" placeholder="ایمیل خود را وارد کنید." className="login_input" onChange={this.handleSid} required/>

                        <br/>

                        <label for="password"><b>رمز عبور</b></label>
                        <input type="password" placeholder="رمز خود را وارد کنید." className="login_input" required/>
                        <button type="button" className="login_button">ثبت نام</button>

                        <button type="button" onClick={this.handleLogin} className="login_button">صفحه ورود</button>
                    </div>
                </form>
            </div>
        );
    }
}

class SignupPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <SignupPart />
                <BolbolestanFooter />
            </div>
        );
    }
}
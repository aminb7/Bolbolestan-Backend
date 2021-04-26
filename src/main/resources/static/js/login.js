class LoginPart extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (<h1>Login</h1>);
    }
}

class LoginPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BolbolestanHeader />
                <LoginPart />
                <BolbolestanFooter />
            </div>
        );
    }
}
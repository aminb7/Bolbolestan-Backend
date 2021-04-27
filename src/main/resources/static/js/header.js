function BolbolestanLogo() {
    return (
        <img className="navbar-brand header_logo" src="../images/logo.png" alt="logo"></img>
    );
}

class HeaderText extends React.Component {
    constructor(props) {
        super(props);
        this.handleFirstPage = this.handleFirstPage.bind(this);
        this.handleSecondPage = this.handleSecondPage.bind(this);
    }

    handleFirstPage(event) {
        if (this.props.text1 == 'انتخاب واحد')
            ReactDOM.render(<CoursesPage />, document.getElementById('app'));
        else if (this.props.text1 == 'خانه')
            ReactDOM.render(<HomePage />, document.getElementById('app'));
        else if (this.props.text1 == 'برنامه هفتگی')
            ReactDOM.render(<PlanPage />, document.getElementById('app'));
    }

    handleSecondPage(event) {
        if (this.props.text2 == 'انتخاب واحد')
            ReactDOM.render(<CoursesPage />, document.getElementById('app'));
        else if (this.props.text2 == 'خانه')
            ReactDOM.render(<HomePage />, document.getElementById('app'));
        else if (this.props.text2 == 'برنامه هفتگی')
            ReactDOM.render(<PlanPage />, document.getElementById('app'));
    }

    render() {
        return (
            <div className="navbar-collapse collapse w-100 order-1 order-md-0 dual-collapse2">
                <ul className="navbar-nav mr-auto order-0">
                    <li className="nav-item active" onClick={this.handleFirstPage}>
                        <a className="nav-link header_text">{this.props.text1}</a>
                    </li>
                    <li className="nav-item" onClick={this.handleSecondPage}>
                        <a className="nav-link header_text">{this.props.text2}</a>
                    </li>
                </ul>
            </div>
        );
    }
}

class LogoutSection extends React.Component {
    constructor(props) {
        super(props);
        this.handleModal = this.handleModal.bind(this);
    }

    handleModal(event) {
        ReactDOM.render(<ModalHomePage src={this.props.src}/>, document.getElementById('app'));
    }

    render() {
        return (
            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav ml-auto order-3">
                    <li className="nav-item active" onClick={this.handleModal}>
                        <a className="nav-link header_logout_text">خروج</a>
                    </li>
                    <li className="nav-item" onClick={this.handleModal}>
                        <a className="nav-link"><img className="header_logout_icon"
                                                     src="../images/icons/001-log-out.png" alt="log out"></img></a>
                    </li>
                </ul>
            </div>
        );
    }
}

class BolbolestanHeader extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="row sticky-top header_border">
                <nav className="col-lg-12 navbar navbar-expand-md header_color">
                    <BolbolestanLogo/>
                    <HeaderText text1={this.props.text1} text2={this.props.text2}/>
                    <LogoutSection src={this.props.src}/>
                </nav>
            </div>
        );
    }
}

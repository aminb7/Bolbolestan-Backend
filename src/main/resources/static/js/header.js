function BolbolestanHeader() {
    return (
        <div className="row sticky-top header_border">
            <nav className="col-lg-12 navbar navbar-expand-md header_color">
                <img className="navbar-brand header_logo" src="../images/logo.png" alt="logo"></img>

                    <div className="navbar-collapse collapse w-100 order-1 order-md-0 dual-collapse2">
                        <ul className="navbar-nav mr-auto order-0">
                            <li className="nav-item active">
                                <a className="nav-link header_text">خانه</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link header_text">انتخاب واحد</a>
                            </li>
                        </ul>
                    </div>

                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav ml-auto order-3">
                            <li className="nav-item active">
                                <a className="nav-link header_logout_text">خروج</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link"><img className="header_logout_icon" src="../images/icons/001-log-out.png" alt="log out"></img></a>
                            </li>
                        </ul>
                    </div>
            </nav>
        </div>
    );
}

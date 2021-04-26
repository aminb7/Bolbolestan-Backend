function ScheduleHeader(props) {
    return (
        <tr className="table_border">
            <th colSpan="7">
                <nav className="navbar navbar-expand-md">
                    <img className="calendar_logo" src="../images/icons/007-calendar.png" alt="calendar"></img>

                    <div className="navbar-collapse collapse w-100 order-1 order-md-0 dual-collapse2">
                        <div className="navbar-nav mr-auto order-0 nav-item active">
                            <a className="nav-link calendar_header_text">برنامه هفتگی</a>
                        </div>
                    </div>

                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav ml-auto order-3">
                            <li className="nav-item">
                                <a className="nav-link calendar_header_text">ترم6 </a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </th>
        </tr>
    );
}

function DaysRow(props) {
    return (
        <tr className="calendar_text">
            <td></td>
            <td>شنبه</td>
            <td>یکشنبه</td>
            <td>دوشنبه</td>
            <td>سه شنبه</td>
            <td>چهارشنبه</td>
            <td>پنجشنبه</td>
        </tr>
    );
}

function TableCell(props) {
    return (
        <p></p>
    );
}

class WeeklySchedule extends React.Component {
    constructor(props) {
        super(props);
        var initialSelectedCourses = {};
        this.state = {selectedCourses: initialSelectedCourses, isLoggedin: false};
    }

    componentWillMount() {
        fetch('loggedin_student')
            .then(resp => resp.json())
            .then(data => {
                this.setState(prevState => ({selectedCourses: data.selectedCourses, isLoggedin: true}));
            });
    }

    render() {
        const timeSlots = ["۹:۰۰ - ۸:۰۰", "۱۰:۰۰ - ۹:۰۰", "۱۱:۰۰ - ۱۰:۰۰", "۱۲:۰۰ - ۱۱:۰۰", "۱۳:۰۰ - ۱۲:۰۰",
            "۱۴:۰۰ - ۱۳:۰۰", "۱۵:۰۰ - ۱۴:۰۰", "۱۶:۰۰ - ۱۵:۰۰", "۱۷:۰۰ - ۱۶:۰۰", "۱۸:۰۰ - ۱۷:۰۰"];

        const dayNumbers = [1,2,3,4,5,6];
        return (
            <div className="row skedule_section">
                <table className="col-lg-12 skedule table_border">
                    <ScheduleHeader />
                    <DaysRow />
                    {timeSlots.map((timeSlot, index) => (
                        <tr className="calendar_text">
                            <tr className="calendar_text">
                                <td>{timeSlot}</td>
                                {dayNumbers.map((dayNumber) => (
                                    <td>
                                        <TableCell dayNum={dayNumber} timeNum={index}/>
                                    </td>
                                ))}
                            </tr>
                        </tr>
                    ))}
                </table>
            </div>
        );
    }
}

class PlanPage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BolbolestanHeader />
                <WeeklySchedule />
                <BolbolestanFooter />
            </div>
        );
    }
}

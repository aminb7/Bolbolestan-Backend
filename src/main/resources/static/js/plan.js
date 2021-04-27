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

function getTimeIndex(time) {
    var timeIndex = time.replaceAll(':', '');
    timeIndex = timeIndex.substring(0, timeIndex.length - 2);
    timeIndex = (Number(timeIndex.substring(timeIndex.length - 2)) / 60)
        + Number(timeIndex.substring(0, timeIndex.length - 2)) - 7;

    return timeIndex;
}

function haveSameTime(start, end, tableTimeNum) {
    var startIndex = getTimeIndex(start);
    return (startIndex >= tableTimeNum) && (startIndex < tableTimeNum + 1);
}

function haveSameDay(courseDays, tableDayNumber) {
    var daysNumbers = courseDays.map(function (day){
        switch (day) {
            case "Saturday": return 1;
            case "Sunday": return 2;
            case "Monday": return 3;
            case "Tuesday": return 4;
            case "Wednesday": return 5;
            case "Thursday": return 6;
        }
    })

    return daysNumbers.includes(Number(tableDayNumber));
}

function courseTypeStyle(type) {
    switch (type) {
        case "Asli": return "main_course_border";
        case "Umumi": return "general_course_border";
        case "Takhasosi": return "specialized_course_border";
        case "Paaye": return "basic_course_border";
    }
}

function TableCell(props) {
    if (!props.isLoggedin)
        return (<p></p>);

    var matchedCourse;
    var isMatched = false;
    var coursesList = Object.values(props.courses);
    for (var i = 0; i < coursesList.length; i++) {
        if (haveSameDay(coursesList[i].course.classTime.days, props.dayNum)
                && haveSameTime(coursesList[i].course.classTime.start, coursesList[i].course.classTime.end, props.timeNum)) {
            isMatched = true;
            matchedCourse = coursesList[i];
            break;
        }
    }

    if (!isMatched)
        return (<p></p>);

    var startIndex = getTimeIndex(matchedCourse.course.classTime.start);
    var endIndex = getTimeIndex(matchedCourse.course.classTime.end);

    var filteredStart = String(matchedCourse.course.classTime.start).substr(0, String(matchedCourse.course.classTime.start).length - 3);
    var filteredEnd = String(matchedCourse.course.classTime.end).substr(0, String(matchedCourse.course.classTime.end).length - 3);
    var height = (endIndex - startIndex) * 48;

    var style = (startIndex - Math.trunc(startIndex) === 0) ? {height: (String(height)+"px")} : {height: (String(height)+"px"), top: "30px"};

    return (
        <p className={courseTypeStyle(matchedCourse.course.type)} style={style}>
            {filteredStart + "-" + filteredEnd}<br />{matchedCourse.course.name}<br />{matchedCourse.course.type}
        </p>
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
        const timeSlots = ["۸:۰۰ - ۷:۰۰", "۹:۰۰ - ۸:۰۰", "۱۰:۰۰ - ۹:۰۰", "۱۱:۰۰ - ۱۰:۰۰", "۱۲:۰۰ - ۱۱:۰۰", "۱۳:۰۰ - ۱۲:۰۰",
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
                                        <TableCell dayNum={dayNumber} timeNum={index}
                                                   courses={this.state.selectedCourses} isLoggedin={this.state.isLoggedin}/>
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
                <BolbolestanHeader src="plan" text2="انتخاب واحد" text1="خانه" />
                <WeeklySchedule />
                <BolbolestanFooter />
            </div>
        );
    }
}

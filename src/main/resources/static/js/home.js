function CarouselIndicators() {
    return (
        <ol className="carousel-indicators">
            <li data-target="#carouselIndicators" data-slide-to="0" className="active"></li>
            <li data-target="#carouselIndicators" data-slide-to="1"></li>
        </ol>
    );
}

function CarouselImages() {
    return (
        <div className="carousel-inner">
            <div className="carousel-item active">
                <img src="../images/cover photo.jpg" className="d-block w-100" alt="..."></img>
            </div>
            <div className="carousel-item">
                <img src="../images/cover photo.jpg" className="d-block w-100" alt="..."></img>
            </div>
        </div>
    );
}

function CarouselControllers() {
    return (
        <div>
            <a className="carousel-control-prev" href="#carouselIndicators" role="button" data-slide="prev">
                <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                <span className="sr-only">Previous</span>
            </a>
            <a className="carousel-control-next" href="#carouselIndicators" role="button" data-slide="next">
                <span className="carousel-control-next-icon" aria-hidden="true"></span>
                <span className="sr-only">Next</span>
            </a>
        </div>
    );
}

function BolbolestanCarousel() {
    return (
        <div id="carouselIndicators" className="carousel slide" data-ride="carousel">
            <CarouselIndicators />
            <CarouselImages />
            <CarouselControllers />
        </div>
    );
}

function ProfileImage(props) {
    return (
        <img className="aside_margin profile_image" src={props.img}
             style={{width: "6cm", height: "6cm", border: "1px solid #008fe0", borderRadius: "100%"}} alt="picture"></img>
    );
}

function StudentDetails(props) {
    return (
        <aside className="col-lg-4 aside_text aside_border">
            <ProfileImage img={props.img}/>
            <p><span className="aside_title_text">نام: </span>{props.name}</p>
            <p><span className="aside_title_text">شماره دانشجویی: </span>{props.id}</p>
            <p><span className="aside_title_text">تاریخ تولد: </span>{props.birthDate}</p>
            <p><span className="aside_title_text">معدل کل: </span>{props.gpa}</p>
            <p><span className="aside_title_text">واحد گذرانده: </span>{props.totalPassedUnits}</p>
            <p><span className="aside_title_text">دانشکده: </span>{props.faculty}</p>
            <p><span className="aside_title_text">رشته: </span>{props.field}</p>
            <p><span className="aside_title_text">مقطع: </span>{props.level}</p>
            <p className="aside_title_text student_state_text">{props.status}</p>
        </aside>
    );
}

function TermGradesTable(props) {
    var total = 0;
    for(var i = 0; i < props.courses.length; i++) {
        total += props.courses[i].grade;
    }
    var avg = total / props.courses.length;
    return (
        <fieldset className="workbook">
            <legend className="legend_text">کارنامه - ترم {props.courses[0].term}</legend>
            <table className="table_text">
                {props.courses.map((gradedCourse, index) => (
                    <tr>
                        <td>{1 + index}</td>
                        <td>{gradedCourse.course.code + "-" + gradedCourse.course.classCode}</td>
                        <td>{gradedCourse.course.name}</td>
                        <td>{gradedCourse.course.units + " واحد"}</td>
                        <td><span className={(Number(gradedCourse.grade) >= 10) ? "succeeded_color succeeded_border" : "failed_color failed_border"}>
                            {(Number(gradedCourse.grade) >= 10) ? "قبول" : "مردود"}</span></td>
                        <td className={(Number(gradedCourse.grade) >= 10) ? "succeeded_color" : "failed_color"}>{gradedCourse.grade}</td>
                    </tr>
                ))}
            </table>
            <p className="total_average">{"معدل: " + String(avg)}</p>
        </fieldset>
    );
}

function GradesSection(props) {
    if (!props.isLoggedin)
        return (<h1>grades section</h1>);

    var coursesInTerms = Object.values(props.gradedCources).reduce(function(r, o){
        var k = o.term;   // unique `loc` key
        if (r[k] || (r[k]=[])) r[k].push({term:k, code: o.code, course: o.course, grade: o.grade});
        return r;
    }, {});

    return (
        <div className="col-lg-8 ">
            <section className="workbook_section">
                {Object.values(coursesInTerms).map(termCourses => (
                    <TermGradesTable courses={termCourses}/>
                ))}
            </section>
        </div>
    );
}

class ProfileSection extends React.Component {
    constructor(props) {
        super(props);
        var initialStudent = {name: "", secondName: "", id: 0, birthDate: "0/0/0", gpa: 0, totalPassedUnits: 0,
            faculty: "", field: "", level: "", status: "", img: "", gradedCourses: {}}
        this.state = {student: initialStudent, isLoggedin: false};
    }

    componentWillMount() {
        fetch('loggedin_student')
            .then(resp => resp.json())
            .then(data => {
                this.setState(prevState => ({student: data, isLoggedin: true}));
            });
    }

    render() {
        return (
            <div className="row">
                <StudentDetails img={this.state.student.img} name={this.state.student.name + " " + this.state.student.secondName} id={this.state.student.id}
                        birthDate={this.state.student.birthDate} gpa={this.state.student.gpa}
                        totalPassedUnits={this.state.student.totalPassedUnits} faculty={this.state.student.faculty}
                        field={this.state.student.field} level={this.state.student.level} status={this.state.student.status}/>
                <GradesSection isLoggedin={this.state.isLoggedin} gradedCources={this.state.student.gradedCourses}/>
            </div>
        );
    }
}

class HomePage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <BolbolestanHeader src="home" text1="انتخاب واحد" text2="برنامه هفتگی" />
                <BolbolestanCarousel />
                <ProfileSection />
                <BolbolestanFooter />
            </div>
        );
    }
}
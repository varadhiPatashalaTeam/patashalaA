package in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddClassTeacher.AddClassTeacherActivity;
import in.varadhismartek.patashalaerp.Birthday.BirthdayActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.AssessmentModuleActivity;
import in.varadhismartek.patashalaerp.DashboardModule.AttendanceList;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.EmployeeActivity;
import in.varadhismartek.patashalaerp.DashboardModule.FeesModule.FeesDetailsActivity;
import in.varadhismartek.patashalaerp.DashboardModule.House.House_Activity;
import in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard.NoticeBoardActivity;
import in.varadhismartek.patashalaerp.DashboardModule.NotificationModule.NotificationActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Syllabus.SyllabusActivity;
import in.varadhismartek.patashalaerp.DashboardModule.TransportModule.TransportActivity;
import in.varadhismartek.patashalaerp.DashboardModule.extraUtils.ModuleModel;
import in.varadhismartek.patashalaerp.GalleryModule.GalleryActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.ScheduleModule.ScheduleActivity;
import in.varadhismartek.patashalaerp.StudentModule.StudentModuleActivity;
import in.varadhismartek.patashalaerp.TicketRaise.StaffTicketActivity;
import in.varadhismartek.patashalaerp.Timetable.TimeTableMain_Activity;
import in.varadhismartek.patashalaerp.Visitors.VisitiorsActivity;

public class FragmentDasboardAdapter extends RecyclerView.Adapter<FragmentDasboardAdapter.FragmentDashboardViewHolder> {
    Context context;
    ArrayList<String> name = new ArrayList<>();
    List<ModuleModel> nameModule;
    public FragmentDasboardAdapter(Context context, List<ModuleModel> nameModule) {
        this.context = context;
        this.nameModule = nameModule;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public FragmentDashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FragmentDashboardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FragmentDashboardViewHolder holder, int position) {
        holder.tvModuleName.setText(nameModule.get(position).getName());
        holder.iv_img.setBackgroundResource(nameModule.get(position).getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String menuName = holder.tvModuleName.getText().toString();

                  if (menuName.equalsIgnoreCase("Schedule")) {
                    Intent intent = new Intent(context, ScheduleActivity.class);
                    context.startActivity(intent);


                }  else if (menuName.equalsIgnoreCase("Fees")) {
                    Intent intent = new Intent(context, FeesDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }

                else if (menuName.equalsIgnoreCase("TimeTable")) {
                    Intent intent = new Intent(context, TimeTableMain_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else if (menuName.equalsIgnoreCase("Attendance")) {
                    Intent intent = new Intent(context, DashBoardMenuActivity.class);
                    intent.putExtra("Fragment_Type", "AttendanceMain");
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Staff Attendance")) {
                    Intent intent = new Intent(context, AttendanceList.class);
                    intent.putExtra("Fragment_Type", "AttendanceMain");
                    context.startActivity(intent);

                }  else if (menuName.equalsIgnoreCase("Student Attendance")) {
                    Intent intent = new Intent(context, StudentModuleActivity.class);
                    intent.putExtra("Fragment_Type", "Student_Attendance");
                    context.startActivity(intent);

                }

                else if (menuName.equalsIgnoreCase("School Activity Barriers")) {
                    Intent intent = new Intent(context, AssessmentModuleActivity.class);
                    context.startActivity(intent);

                }

                /************************Final ***************************/

                else if (menuName.equalsIgnoreCase("School Profile")) {
                    Intent intent = new Intent(context, DashBoardMenuActivity.class);
                      intent.putExtra("Fragment_Type", "School_Profile");
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Staff Directory")) {
                    Constant.CLICK_BY = "STAFF";
                    Intent intent = new Intent(context, EmployeeActivity.class);
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Student Directory")) {
                    Intent intent = new Intent(context, StudentModuleActivity.class);
                    Constant.CLICK_BY = "STUDENT";
                    intent.putExtra("Fragment_Type", "StudentList");
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Subject")) {
                    Intent intent = new Intent(context, DashBoardMenuActivity.class);
                    intent.putExtra("Fragment_Type", "Subject");
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Leave")) {
                    Intent intent = new Intent(context, DashBoardMenuActivity.class);
                    intent.putExtra("Fragment_Type", "LeaveDashBoard");
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Question Bank")) {
                    Intent intent_ques_bank = new Intent(context, DashBoardMenuActivity.class);
                    intent_ques_bank.putExtra("Fragment_Type","QUESTIONBANK_LIST");
                    context.startActivity(intent_ques_bank);

                }
                else if (menuName.equalsIgnoreCase("Gallery")) {
                    Intent intent = new Intent(context, GalleryActivity.class);
                    context.startActivity(intent);

                }

                else if (menuName.equalsIgnoreCase("Homework")) {
                    Intent intent = new Intent(context, DashBoardMenuActivity.class);
                    intent.putExtra("Fragment_Type", "HomeWorkInbox");
                    context.startActivity(intent);

                }
                  else if (menuName.equalsIgnoreCase("House")) {
                      Intent house = new Intent(context, House_Activity.class);
                      context.startActivity(house);

                  }

                else if (menuName.equalsIgnoreCase("Notice Board")) {
                    Intent intent = new Intent(context, NoticeBoardActivity.class);
                    context.startActivity(intent);

                }

                else if (menuName.equalsIgnoreCase("Visitor")) {
                    Intent intent = new Intent(context, VisitiorsActivity.class);
                    context.startActivity(intent);

                }  else if (menuName.equalsIgnoreCase("Syllabus")) {
                    Intent intent = new Intent(context, SyllabusActivity.class);
                    context.startActivity(intent);

                } else if (menuName.equalsIgnoreCase("Class Teacher")) {
                    Intent intent = new Intent(context, AddClassTeacherActivity.class);
                    context.startActivity(intent);

                }else if (menuName.equalsIgnoreCase("Birthday")) {
                    Intent intent = new Intent(context, BirthdayActivity.class);
                    context.startActivity(intent);

                }
                else if (menuName.equalsIgnoreCase("Notification")) {
                    Intent intent = new Intent(context, NotificationActivity.class);
                 //   intent.putExtra("Fragment_Type", "Notification");
                    context.startActivity(intent);

                }else if (menuName.equalsIgnoreCase("Transport")){
                      Intent intent = new Intent(context, TransportActivity.class);
                      context.startActivity(intent);
                  }

                  else if (menuName.equalsIgnoreCase("Ticket Raise")){
                      Intent intent = new Intent(context, StaffTicketActivity.class);
                      context.startActivity(intent);
                  }




            }
        });
    }

    @Override
    public int getItemCount() {
        return nameModule.size();
    }

    public class FragmentDashboardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvModuleName;
        public ImageView iv_img;

        public FragmentDashboardViewHolder(View itemView) {
            super(itemView);
            tvModuleName = itemView.findViewById(R.id.tvMenu);
            iv_img = itemView.findViewById(R.id.iv_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

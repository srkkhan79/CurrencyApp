package currency.recognize.currencyrecog.Externalfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import currency.recognize.currencyrecog.R;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * Created by Administrator on 25/03/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> messages;
    private Context context;
    MediaController mediaController;


    public MessageAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View V= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_view,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(V);
        return viewHolder;
    }

    public String[] detectSentences(String paragraph) throws IOException {

        InputStream modelIn =context.getAssets().open("en-sent.bin");
        final SentenceModel sentenceModel = new SentenceModel(modelIn);
        modelIn.close();
        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        String sentences[] = sentenceDetector.sentDetect(paragraph);
        for (String sent : sentences) {
          //  Log.i("ASK",sent);
        }
        return sentences;
    }

    String[] sentences;
    String[] tags;
    String[] word;

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        String msg="";
        public BackgroundTask(Context context,String msg) {
            dialog = new ProgressDialog(context);
            this.msg=msg;
        }


        @Override
        protected void onPreExecute() {
            dialog.setMessage("Preparing Video, please wait.");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            playvideo();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                sentences=null;
                tags=null;
                word=null;
                List<String> tagarray=new ArrayList<String>();
                List<String> wordarray=new ArrayList<String>();

                System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
                AssetFileDescriptor fileDescriptor =context.getAssets().openFd("enposmaxent.bin");
                FileInputStream inputStream = fileDescriptor.createInputStream();
                POSModel posModel = new POSModel(inputStream);
                POSTaggerME posTaggerME = new POSTaggerME(posModel);

                sentences = detectSentences(msg);



                for (String sentence : sentences) {
                    String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
                    String[] mytags = posTaggerME.tag(whitespaceTokenizerLine);
                    for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
                        String worda = whitespaceTokenizerLine[i].trim();
                        String tag = mytags[i].trim();
                        tagarray.add(tag);
                        wordarray.add(worda);
                     //   Log.i("ASK", tag + ":" + worda + " ");
                    }
                    tagarray.add("*");
                    wordarray.add("*");
                }

                tags=new String[tagarray.size()];
                word=new String[wordarray.size()];

                tags=tagarray.toArray(tags);
                word=wordarray.toArray(word);


            } catch (Exception e) {
                Log.e("ASK",e.getClass().getName().toString() +e.getMessage().toString());
            }

            return null;
        }

    }


    @Override
    public void onBindViewHolder(final MessageAdapter.ViewHolder viewHolder, int i) {
        final Message msg=messages.get(i);
        viewHolder.editfrom.setText(msg.getFrom());
        viewHolder.edittype.setText(msg.getType());
        viewHolder.editmessage.setText(msg.getMessage());
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        viewHolder.editdate.setText(simpleDate.format(msg.getDate()));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message ms=messages.get(viewHolder.getAdapterPosition());
                if(ms.getType().equals("text")) {
                    BackgroundTask task = new BackgroundTask(context, ms.getMessage());
                    task.execute();
                }
            }
        });

    }

    public void playvideo()
    {

//        Intent videoplay=new Intent(context,videoplayer.class);
//        videoplay.putExtra("sentense",sentences);
//        videoplay.putExtra("tags",tags);
//        videoplay.putExtra("words",word);
//        context.startActivity(videoplay);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView editfrom,edittype,editmessage,editdate;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            editfrom=(TextView)itemView.findViewById(R.id.editfrom);
            edittype=(TextView)itemView.findViewById(R.id.edittype);
            editmessage=(TextView)itemView.findViewById(R.id.editmessage);
            editdate=(TextView)itemView.findViewById(R.id.editdate);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.msglinearlayout);
        }
    }
}
